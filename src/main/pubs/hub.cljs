(ns pubs.hub
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]
            [re-frame.core :as rf :refer [dispatch dispatch-sync]]
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
  (do-get db (ver-route db (str "/files/" id)) :res/rm-file http/delete [k id]))

(defn ls-files [db]
  (do-get db (prj-route db "/files") :res/ls-files))

(defn usage [db files]
  (do-post db (prj-route db "/usage") :res/usage http/post files))

 (defn authors [db]
  (do-get db (ver-route db "/authors") :res/authors))

(defn owners [db]
  (do-get db (prj-route db "/owners") :res/owners))
 
