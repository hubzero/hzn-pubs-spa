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

(defn ver-route [db]
  (str "/"
       "pubs/"
       (get-in db [:data :pub-id])
       "/v/"
       (get-in db [:data :ver-id])
       )  
  )

(defn handle [res s event-key]
  (if (= 200 (:status res))
    (dispatch [event-key (:body res)])
    (dispatch [:err (:status res)])
   )
  )

(defn do-get [db route event-key]
  (go (-> route
          get-url
          http/get
          <!
          (handle db event-key)
          ))
  db
  )

(defn me [db] (do-get db "/users/me" :res/me))
(defn master-types [db] (do-get db "/types" :res/master-types))
(defn pub [db] (do-get db (ver-route db) :res/pub))
 
