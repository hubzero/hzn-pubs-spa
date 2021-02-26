(ns pubs.hub
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]
            [re-frame.core :as rf :refer [dispatch dispatch-sync]]
            [pubs.utils :as utils]
            )
  )

(defn get-url [route]
  (str (-> js/window .-location .-protocol)
       "//"
       (-> js/window .-location .-host)
       "/p"
       route
       )
  )

(defn ver-route [db & [route]]
  (str "/"
       "pubs/"
       (get-in db [:data :pub-id])
       "/v/"
       (get-in db [:data :ver-id])
       route
       )  
  )

(defn prj-route [db & [route]]
  (str "/prjs/"
       (get-in db [:data :prj-id])
       route
       )  
  )

(defn handle [res s event-key args]
  (if (= 200 (:status res))
    (if (vector? args)
      (dispatch (into [] (concat [event-key (:body res)] args)))
      (dispatch [event-key (:body res) args])
      )
    (dispatch [:err (:status res)])
    )
  )

(defn req [db route event-key method args]
  (go (-> route
          get-url
          ((or method http/get))
          <!
          (handle db event-key args)
          ))
  db
  )

(defn do-get [db route event-key & [method args]]
  (req db route event-key method args) 
  )

(defn do-post [db route event-key & [method data]]
  (go (-> route
          get-url
          ((or method http/post) {:edn-params data})
          <!
          (handle db event-key data)
          ))
  db
  )

(defn me [db]
  (do-get db "/users/me" :res/me))

(defn master-types [db]
  (do-get db "/types" :res/master-types))

(defn pub [db]
  (do-get db (ver-route db) :res/pub))

(defn prj [db]
  (do-get db (prj-route db) :res/prj))

(defn files [db]
  (do-get db (ver-route db "/files") :res/files))

(defn add-file [db file]
  (do-post db (ver-route db "/files") :res/add-file http/post file))

(defn rm-file [db k id]
  (do-get db
          (ver-route db (str "/files/" id))
          :res/rm-file
          http/delete
          [k id]))

(defn update-file
  "Make a call to the API to update the list.

   Causes a re-frame trigger event as well."
  [db file k]
  (when
    (not= file (get-in db [:data :content (utils/->keyword (:id file))]))
    (do-post db
             (ver-route db (str "/files/" (:id file)))
             :res/update-file
             http/put
             file)))

(defn ls-files [db]
  (do-get db (prj-route db "/files") :res/ls-files))

(defn usage [db files]
  (do-post db (prj-route db "/usage") :res/usage http/post files))

(defn authors [db]
  (do-get db (ver-route db "/authors") :res/authors))

(defn owners [db]
  (do-get db (prj-route db "/owners") :res/owners))

(defn rm-author [db id]
  (do-get db (ver-route db (str "/authors/" id)) :res/rm-author http/delete id))

(defn update-author [db author]
  (when (not= author (get-in db [:data :authors-list (utils/->keyword (:id author))]))
    (do-post db
             (ver-route db (str "/authors/" (:id author)))
             :res/update-author
             http/put
             author)))

(defn add-author [db author]
  (do-post db
           (ver-route db "/authors")
           :res/add-author
           http/post
           author))

(defn new-author [db author]
  (do-post db
           (ver-route db "/authors/new")
           :res/new-author
           http/post
           author))

(defn search-users [db v]
  (do-post db
           "/users/search"
           :res/search-users
           http/post
           {:q v}
           ))

(defn tags [db]
  (do-get db (ver-route db "/tags") :res/tags))

(defn rm-tag [db id]
  (do-get db (ver-route db (str "/tags/" id)) :res/rm-tag http/delete id))

(defn add-tag [db tag-str]
  (do-post db
           (ver-route db "/tags")
           :res/add-tag
           http/post
           {:tag tag-str}))

(defn search-tags [db tag-str]
  (do-post db
           "/tags/search"
           :res/search-tags
           http/post
           {:q tag-str})
  )

(defn licenses [db]
  (do-get db "/licenses" :res/licenses)
  )

(defn license [db]
  (as-> db $
    (get-in $ [:data :license_type])
    (str "/licenses/" $)
    (do-get db $ :res/license)
    )
  )

(defn save-pub [db pub & [response-handler]]
  (do-post db
           "/pubs"
           (or response-handler :res/save-pub)
           http/post
           pub)
  )

(defn citations [db]
  (do-get db (ver-route db "/citations") :res/citations)
  )

(defn add-citation [db c]
  (do-post db (ver-route db "/citations") :res/add-citation http/post c)
  )

(defn search-citations [db v]
  (do-post db "/citations/search" :res/search-citations http/post {:doi v})
  )

(defn create-citation [db c]
  (do-post db "/citations" :res/create-citation http/post c)
  )

(defn rm-citation [db id]
  (do-get db (ver-route db (str "/citations/" id)) :res/rm-citation http/delete id))

(defn citation-types [db]
  (do-get db "/citations/types" :res/citation-types)
  )

(defn ls-dbs [db]
  (do-get db (prj-route db "/databases") :res/ls-dbs))

(defn dbs [db]
  (do-get db (ver-route db "/databases") :res/dbs))

(defn add-db [db database]
  (do-post db (ver-route db "/databases") :res/add-db http/post database))

(defn rm-db [db k id]
  (do-get db
          (ver-route db (str "/databases/" id))
          :res/rm-db
          http/delete
          [k id]))

(defn update-db [db attach k]
  (do-post db
           (ver-route db (str "/databases/" (:id attach)))
           :res/update-db
           http/put
           attach))

(defn search-series [db v]
  (do-post db "/series/search" :res/search-series http/post {:q v})
  )

(defn series [db]
  (do-get db (ver-route db "/series") :res/series))

(defn add-series [db pub]
  (do-post db (ver-route db "/series") :res/add-series http/post pub))

(defn rm-series [db k id]
  (do-get db
          (ver-route db (str "/series/" id))
          :res/rm-series
          http/delete
          [k id]))

(defn update-series [db attach k]
  (do-post db
           (ver-route db (str "/series/" (:id attach)))
           :res/update-series
           http/put
           attach))

