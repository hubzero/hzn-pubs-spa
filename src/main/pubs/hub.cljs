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

(defn prj-route [db]
  (str "/"
       "prjs/"
       (get-in db [:data :prj-id])
       )  
  )


(defn handle [res s event-key args]
  (if (= 200 (:status res))
    (dispatch (into [] (concat [event-key (:body res)] args)))
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
  (req db route event-key http/get args) 
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

(defn rm-file [db k id]
  (do-get db (ver-route db) :res/rm-file http/delete [k id]))
 
