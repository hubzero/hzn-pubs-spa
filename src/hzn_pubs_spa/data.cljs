(ns hzn-pubs-spa.data
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]] 
            [cljs-http.client :as http]   
            [secretary.core :as secretary]
            [hzn-pubs-spa.mutate :as mutate]
            [hzn-pubs-spa.utils :as utils]
            )
  )

(def url (str (-> js/window .-location .-protocol) "//" (-> js/window .-location .-host) "/p"))

(defn- _error [s code]
  (secretary/dispatch! "/error")
  (set! (-> js/window .-location) (str "/pubs?err=" code "&msg=Error"))     
  )

(defn- _handle-res [s res f]
  (if (= (:status res) 200)
    (f s res)
    (_error s (:status res))
    )
  )

(defn options [s]
  {
   ;:with-credentials? true
   :headers {
             "Accept" "application/edn"
             "Content-Type" "application/edn"
             }
   }
  )

(defn get-user [s]
  (go (let [res (<! (http/get (str url "/users/me")
                              (options s)))]
        (_handle-res s res (fn [s res]
                             (swap! s assoc-in [:data :user-id] (:id (:body res)))
                             ))
        ))
  )

(defn add-file
  "s is the state, file is a map - :type, :index, :path, :name - JBG"
  [s file]
  (prn "ADD-FILE" file)
  (go (let [res (<! (http/post (str url
                                    "/pubs/" (get-in @s [:data :pub-id])
                                    "/v/" (get-in @s [:data :ver-id])
                                    "/files")  {:edn-params file}))]
        (prn "FILE ADDED <<<<<< " res)
        (as-> (:body res) $
          (:generated_key $)
          (swap! s assoc-in [:data (:type file) $] (assoc file :id $))
          )
        ))
  )

(defn rm-file
  "s is the state, k is the type - :content, :images, :support-docs, and file-id - JBG"
  [s k file-id]
  (prn "RM-FILE" k file-id)
  (go (let [res (<! (http/delete (str url
                                      "/pubs/" (get-in @s [:data :pub-id])
                                      "/v/" (get-in @s [:data :ver-id])
                                      "/files/" file-id) 
                                 (options s)))]
        (_handle-res s res (fn [s res]
                             (swap! s update-in [:data k] dissoc file-id)
                             ))
        )) 
  )

(defn ls-files [s]
  (go (let [res (<! (http/get (str url "/prjs/" (get-in @s [:data :prj-id]) "/files")
                              (options s)))]
        ;(prn (:body res))
        (swap! s assoc :files (cljs.reader/read-string (:body res)))
        (swap! s assoc-in [:ui :current-folder] [["Project files" (first (first (:files @s)))]])
        ))
  )

(defn get-files [s]
  (go (let [res (<! (http/get (str url
                                   "/pubs/" (get-in @s [:data :pub-id])
                                   "/v/" (get-in @s [:data :ver-id])
                                   "/files")
                              (options s)))]
        (prn (:body res))
        (_handle-res s res (fn [s res]
                             (swap! s update :data merge (:body res))
                             ))

        ))
  )


(defn get-owners [s]
  (go (let [res (<! (http/get (str url "/prjs/" (get-in @s [:data :prj-id]) "/owners")
                              (options s)))]
        (->>
          (:body res)
          (map (fn [u] [(:id u) u]))
          (into {})
          (swap! s assoc :users))
        )
      )
  )

(defn search-users [s]
  (go (let [res (<! (http/post (str url "/users/search")
                               {:edn-params {:q (:user-query @s)}}
                               ))]
        (prn "USER RESPONSE" (:body res))
        (swap! s assoc :user-results (:body res))
        ))
  )

(defn get-authors [s]
  (go (let [res (<! (http/get (str url
                                   "/pubs/" (get-in @s [:data :pub-id])
                                   "/v/" (get-in @s [:data :ver-id])
                                   "/authors")
                              (options s)))]
        (_handle-res s res (fn [s res]
                             (swap! s assoc-in [:data :authors-list] (:body res))
                             ))

        ))
  )

(defn add-author
  "s is the state, author is a map - JBG"
  [s author]
  (go (let [res (<! (http/post (str url
                                    "/pubs/" (get-in @s [:data :pub-id])
                                    "/v/" (get-in @s [:data :ver-id])
                                    "/authors")  {:edn-params author}))]
        (prn "<<< AUTHOR" (:body res))
        (get-authors s)
        ))
  )

(defn new-author
  [s author]
  (prn "AUTHOR >>>" author)
  (go (let [res (<! (http/post (str url
                                    "/pubs/" (get-in @s [:data :pub-id])
                                    "/v/" (get-in @s [:data :ver-id])
                                    "/authors/new") {:edn-params author}))]
        (prn "<<< OWNER" (:body res))
        (as-> (:body res) $
          (:generated_key $)
          (assoc author :id $)
          (swap! s assoc-in [:data :authors-list (:id $)] $)
          )
        ))

  )

(defn update-author
  [s author]
  (prn "UPDATE AUTHOR >>>>" author)
  (go (let [res (<! (http/put (str url
                                   "/pubs/" (get-in @s [:data :pub-id])
                                   "/v/" (get-in @s [:data :ver-id])
                                   "/authors/" (:id author)) {:edn-params author}))]
        (_handle-res s res (fn [s res]
                             (prn "<<< UPDATED AUTHOR" (:body res))
                             (get-authors s)
                             ))
        ))
  )

(defn rm-author 
  "s is the state, and author-id - JBG"
  [s author-id]
  (prn "REMOVING AUTHOR" author-id)
  (go (let [res (<! (http/delete (str url
                                      "/pubs/" (get-in @s [:data :pub-id])
                                      "/v/" (get-in @s [:data :ver-id])
                                      "/authors/" author-id) 
                                 (options s)))]
        (_handle-res s res (fn [s res]
                             (swap! s update-in [:data :authors-list] dissoc author-id)
                             ))
        )) 
  )

(defn get-licenses [s]
  (go (let [res (<! (http/get (str url "/licenses") (options s)))]
        ;(prn (:body res))
        (->>
          (cljs.reader/read-string (:body res))
          (swap! s assoc :licenses))
        ))
  )

(defn search-citations [s]
  (go (let [res (<! (http/post (str url "/citations/search") {:edn-params {:doi (:doi-query @s)}}
                               ))]
        (prn "CITATIONS<<<<<<<" (:body res))
        (swap! s assoc :doi-results (:body res)) 
        ))
  )

(defn get-citation-types [s]
  (go (let [res (<! (http/get (str url "/citation-types") (options s)))]
        ;(prn (:body res))
        (->>
          (cljs.reader/read-string (:body res))
          (swap! s assoc :citation-types))
        ))
  )

(defn add-citation
  "s is the state, tag-str is the tag as a str - JBG"
  [s c]
  (prn "ADDING CITATION" c)
  (go (let [res (<! (http/post (str url
                                    "/pubs/" (get-in @s [:data :pub-id])
                                    "/v/" (get-in @s [:data :ver-id])
                                    "/citations")  {:edn-params c}))]
        (prn "<<< CITATION" (:body res))
        (_handle-res s res (fn [s res]
                             (swap! s update-in [:data :citations] assoc (:id c) c)
                             ))
        ))
  )

(defn rm-citation
  "s is the state, and citation-id - JBG"
  [s citation-id]
  (prn "RM CITATION>>>" citation-id)
  (go (let [res (<! (http/delete (str url
                                      "/pubs/" (get-in @s [:data :pub-id])
                                      "/v/" (get-in @s [:data :ver-id])
                                      "/citations/" citation-id) 
                                 (options s)))]
        (prn "<<< RM CITATION" citation-id)
        (_handle-res s res (fn [s res]
                             (swap! s update-in [:data :citations] dissoc citation-id)
                             ))
        ))
  )

(defn create-citation [s]
  (go (let [c (get-in @s [:data :citations-manual])
            res (<! (http/post (str url "/citations") {:edn-params c}))]
        (prn "CREATE CITATION >>>>> " c)
        (prn "<<<< CITATION" (:body res))
        (->>
          (:body res)
          (:generated_key)
          (assoc c :id)
          (add-citation s)
          )
        ;; Clear form - JBG
        (swap! s update :data dissoc :citations-manual)
        ;; Scroll form, am I a dirty hack? ... yes. - JBG
        (-> js/document (.querySelector ".citations-manual .inner") (.scrollTo 0 0)) 
        ))
  )

(defn usage [s]
  (go (let [data (map #(:path %) (vals (get-in @s [:data :content] {})))
            res (<! (http/post (str url "/prjs/" (get-in @s [:data :prj-id]) "/usage" ) {:edn-params data}))]
        (prn "USAGE >>>" data)
        (prn "<<< USAGE" (:body res))
        (swap! s assoc :usage (:body res))
        ))
  )

(defn get-tags [s]
  (go (let [res (<! (http/get (str url
                                   "/pubs/" (get-in @s [:data :pub-id])
                                   "/v/" (get-in @s [:data :ver-id])
                                   "/tags") 
                              (options s)))]
        (prn "<<< TAGS" (:body res))
        (_handle-res s res (fn [s res]
                             (swap! s assoc-in [:data :tags]
                                    (->>
                                      (:body res)
                                      (group-by :id)
                                      (map (fn [[k v]] [k (first v)]))
                                      (into {})
                                      )
                                    )
                             ))
        ))
  )

(defn get-citations [s]
  (go (let [res (<! (http/get (str url
                                   "/pubs/" (get-in @s [:data :pub-id])
                                   "/v/" (get-in @s [:data :ver-id])
                                   "/citations") 
                              (options s)))]
        (prn "<<< CITATIONS" (:body res))
        (_handle-res s res (fn [s res]
                             (swap! s assoc-in [:data :citations]
                                    (->>
                                      (:body res)
                                      (group-by :id)
                                      (map (fn [[k v]] [k (first v)]))
                                      (into {})
                                      )
                                    )
                             ))
        ))
  )


(defn get-license [s]
  (if-let [license-type (get-in @s [:data :license_type])]
    (go (let [res (<! (http/get (str url "/licenses/" license-type)
                                (options s)))]
          (prn "<<< LICENSE" (:body res))
          (_handle-res s res (fn [s res]
                               (swap! s assoc-in [:data :licenses] (:body res))
                               ))
          ))
    )
  )

(defn get-pub [s & [validate?]]
  (go (let [res (<! (http/get (str url
                                   "/pubs/" (get-in @s [:data :pub-id])
                                   "/v/" (get-in @s [:data :ver-id])
                                   ) (options s)))]
        (prn "<<< PUB" (:body res))
        (_handle-res s res (fn [s res]
                             (->> (:body res)
                                  (mutate/coerce)
                                  (swap! s assoc :data)
                                  )
                             (get-files s)
                             (get-authors s)
                             (get-tags s)
                             (get-license s)
                             (get-citations s)
                             (usage s)
                             ))
        ))
  )


(defn save-pub [s & [callback]]
  (as-> (:data @s) $
    (mutate/prepare $)
    (go (let [res (<! (http/post (str url "/pubs") {:edn-params $}))]
          (prn "SENT PUB >>>" $)
          (prn "<<< RECEIVED"(:body res))
          (_handle-res s res (fn [s res]
                               (swap! s update :data merge (:body res))
                               (if callback (callback))
                               ))
          ))
    )
  )

(defn submit-pub [s]
  (as-> (:data @s) $
    (mutate/prepare $)
    (go (let [res (<! (http/post (str url "/pubs") {:edn-params $}))]
          (prn "SENT PUB >>>" $)
          (prn "<<< RECEIVED"(:body res))
          (get-pub s)
          ;(set! (-> js/window .-location) (str "/publications/" (get-in @s [:data :pub-id])))
          ))
    )
  )

(defn save-state [s]
  (go (let [res (<! (http/post (str url "/ui-state") {:edn-params (mutate/coerce-ui-state s)}))]
        (_handle-res s res (fn [s res]
                             (prn "Saved state.")
                             ))
        ))
  )

(defn get-prj [s]
  (go (let [res (<! (http/get (str url "/prjs/" (get-in @s [:data :prj-id])) (options s)))]
        (_handle-res s res (fn [s res]
                             ;; Load the disk usage for the project - JBG
                             (usage s)
                             ))
        ))
  )

(defn add-tag
  "s is the state, tag-str is the tag as a str - JBG"
  [s tag-str]
  (prn "ADDING TAG" tag-str)
  (go (let [res (<! (http/post (str url
                                    "/pubs/" (get-in @s [:data :pub-id])
                                    "/v/" (get-in @s [:data :ver-id])
                                    "/tags")  {:edn-params {:tag tag-str}}))]
        (prn "<<< TAG" (:body res))
        (get-tags s)
        (swap! s assoc-in [:ui :tag] false)
        ))
  )

(defn rm-tag
  "s is the state, and tag-id - JBG"
  [s tag-id]
  (go (let [res (<! (http/delete (str url
                                      "/pubs/" (get-in @s [:data :pub-id])
                                      "/v/" (get-in @s [:data :ver-id])
                                      "/tags/" tag-id) 
                                 (options s)))]
        (_handle-res s res (fn [s res]
                             (swap! s update-in [:data :tags] dissoc tag-id)
                             ))
        ))
  )

(defn search-tags [s]
  (go (let [res (<! (http/post (str url "/tags/search")
                               {:edn-params {:q (:tag-query @s)}}
                               ))]
        (prn "SEARCH TAG RESPONSE" (:body res))
        (swap! s assoc :tag-results (:body res))
        ))
  )

