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


(defn options [s] {})

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
  (go (let [res (<! (http/post (str url
                                    "/pubs/" (get-in @s [:data :pub-id])
                                    "/v/" (get-in @s [:data :ver-id])
                                    "/files")  {:edn-params file}))]
        (as-> (:body res) $
          (:generated_key $)
          (swap! s assoc-in [:data (:type file) $] (assoc file :id $))
          )
        ))
  )

(defn rm-file
  "s is the state, k is the type - :content, :images, :support-docs, and file-id - JBG"
  [s k file-id]
  (prn "RM FILE" file-id)
  (go (let [id (utils/keyword-to-int file-id)
            res (<! (http/delete (str url
                                      "/pubs/" (get-in @s [:data :pub-id])
                                      "/v/" (get-in @s [:data :ver-id])
                                      "/files/"
                                      id) 
                                 (options s)))]
        (_handle-res s res (fn [s res]
                             (swap! s update-in [:data k] dissoc (keyword file-id))
                             ))
        ))
  )

(defn ls-files [s]
  (go (let [res (<! (http/get (str url "/prjs/" (get-in @s [:data :prj-id]) "/files")
                              (options s)))]
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
                               ))] (swap! s assoc :user-results (:body res)))))

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
  (prn "ADD AUTHOR" author)
  (go (let [res (<! (http/post (str url
                                    "/pubs/" (get-in @s [:data :pub-id])
                                    "/v/" (get-in @s [:data :ver-id])
                                    "/authors")  {:edn-params author}))]
        (get-authors s)
        ))
  )

(defn new-author
  [s author]
  (go (let [res (<! (http/post (str url
                                    "/pubs/" (get-in @s [:data :pub-id])
                                    "/v/" (get-in @s [:data :ver-id])
                                    "/authors/new") {:edn-params author}))]
        (as-> (:body res) $
          (:generated_key $)
          (assoc author :id $)
          (swap! s assoc-in [:data :authors-list (:id $)] $)
          )
        ))

  )

(defn update-author
  [s author]
  (go (let [id (utils/keyword-to-int (:id author))
            res (<! (http/put (str url
                                   "/pubs/" (get-in @s [:data :pub-id])
                                   "/v/" (get-in @s [:data :ver-id])
                                   "/authors/"
                                   id) {:edn-params author}))]
        (_handle-res s res (fn [s res]
                             (get-authors s)
                             )))))

(defn rm-author 
  "s is the state, and author-id - JBG"
  [s author-id]
  (prn "RM AUTHOR" author-id)
  (go (let [id (utils/keyword-to-int author-id)
            res (<! (http/delete (str url
                                      "/pubs/" (get-in @s [:data :pub-id])
                                      "/v/" (get-in @s [:data :ver-id])
                                      "/authors/"
                                      id
                                      )
                                 (options s)))]
        (_handle-res s res (fn [s res]
                             (swap! s update-in [:data :authors-list] dissoc (keyword author-id))
                             ))
        ))
  )

(defn get-licenses [s]
  (go (let [res (<! (http/get (str url "/licenses") (options s)))]
        (_handle-res s res (fn [s res]
                             (swap! s assoc :licenses (:body res))
                             ))
        ))
  )

(defn search-citations [s]
  (go (let [res (<! (http/post (str url "/citations/search") {:edn-params {:doi (:doi-query @s)}}
                               ))]
        (_handle-res s res (fn [s res]
                             (swap! s assoc :doi-results (:body res))))))
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
  (go (let [res (<! (http/post (str url
                                    "/pubs/" (get-in @s [:data :pub-id])
                                    "/v/" (get-in @s [:data :ver-id])
                                    "/citations")  {:edn-params c}))]
        (_handle-res s res (fn [s res]
                             (swap! s update-in [:data :citations] assoc (:id c) c)
                             ))
        ))
  )

(defn rm-citation
  "s is the state, and citation-id - JBG"
  [s citation-id]
  (go (let [id (utils/keyword-to-int citation-id)
            res (<! (http/delete (str url
                                      "/pubs/" (get-in @s [:data :pub-id])
                                      "/v/" (get-in @s [:data :ver-id])
                                      "/citations/"
                                      id) 
                                 (options s)))]
        (_handle-res s res (fn [s res]
                             (swap! s update-in [:data :citations] dissoc (keyword citation-id))
                             ))
        ))
  )

(defn create-citation [s]
  (go (let [c (get-in @s [:data :citations-manual])
            res (<! (http/post (str url "/citations") {:edn-params c}))]
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
        (swap! s assoc :usage (:body res))
        ))
  )

(defn get-tags [s]
  (go (let [res (<! (http/get (str url
                                   "/pubs/" (get-in @s [:data :pub-id])
                                   "/v/" (get-in @s [:data :ver-id])
                                   "/tags") 
                              (options s)))]
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
          (_handle-res s res (fn [s res]
                               (swap! s assoc-in [:data :licenses] (:body res))
                               ))
          ))
    )
  )


(defn get-prj [s]
  (go (let [res (<! (http/get (str url "/prjs/" (get-in @s [:data :prj-id])) (options s)))]
        (_handle-res s res (fn [s res]
                             (swap! s assoc-in [:data :prj] (:body res))
                             ;; Load the disk usage for the project - JBG
                             (usage s)
                             ))
        ))
  )

(defn _get-pub [s]
  (go (let [res (<! (http/get (str url
                                   "/pubs/" (get-in @s [:data :pub-id])
                                   "/v/" (get-in @s [:data :ver-id])
                                   ) (options s)))]
        (_handle-res s res (fn [s res]
                             (->> (:body res)
                                  (mutate/coerce s)
                                  (swap! s assoc :data)
                                  )
                             (get-prj s)
                             (get-files s)
                             (get-authors s)
                             (get-tags s)
                             (get-license s)
                             (get-citations s)
                             ))
        ))
  )

(defn get-master-types [s]
  (go (let [res (<! (http/get (str url "/types")
                              (options s)))]
        (_handle-res s res (fn [s res]
                             (->>
                               (:body res)
                               (filter #(some #{(:type %)} ["File(s)" "Databases" "Series"]))
                               (swap! s assoc :master-types)
                               )
                             (_get-pub s)
                             ))
        ))
  )

(defn get-pub [s]
  (get-master-types s)
  )

(defn save-pub [s & [callback]]
  (if (not (= (get-in @s [:data :state]) 1))
    (as-> (:data @s) $
      (mutate/prepare s $)
      (go (let [res (<! (http/post (str url "/pubs") {:edn-params $}))]
            (_handle-res s res (fn [s res]
                                 (swap! s update :data merge (:body res))
                                 (if callback (callback))
                                 ))
            ))
      )  
    )
  )

(defn submit-pub [s]
  (as-> (:data @s) $
    (mutate/prepare s $)
    (go (let [res (<! (http/post (str url "/pubs") {:edn-params $}))]
          (_handle-res s res (fn [s res]
                               (set! (-> js/window .-location) (str "/publications/" (get-in @s [:data :pub-id])))
                               ))
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


(defn add-tag
  "s is the state, tag-str is the tag as a str - JBG"
  [s tag-str]
  (go (let [res (<! (http/post (str url
                                    "/pubs/" (get-in @s [:data :pub-id])
                                    "/v/" (get-in @s [:data :ver-id])
                                    "/tags")  {:edn-params {:tag tag-str}}))]
        (get-tags s)
        (swap! s assoc-in [:ui :tag] false)
        ))
  )

(defn rm-tag
  "s is the state, and tag-id - JBG"
  [s tag-id]
  (go (let [id (utils/keyword-to-int tag-id)
            res (<! (http/delete (str url
                                      "/pubs/" (get-in @s [:data :pub-id])
                                      "/v/" (get-in @s [:data :ver-id])
                                      "/tags/"
                                      id) 
                                 (options s)))]
        (_handle-res s res (fn [s res]
                             (swap! s update-in [:data :tags] dissoc (keyword tag-id))
                             ))
        ))
  )

(defn search-tags [s]
  (go (let [res (<! (http/post (str url "/tags/search")
                               {:edn-params {:q (:tag-query @s)}}
                               ))]
        (swap! s assoc :tag-results (:body res))
        ))
  )

