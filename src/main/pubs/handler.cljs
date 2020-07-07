(ns pubs.handler
  (:require [pubs.db :as db]
    [pubs.hub :as hub])
  )

(defn initialize-db [_ _] db/default-db)

(defn navigated [db [_ new-match]]
  (if-let [f (get-in new-match [:data :controller])]
    (apply f [db (:path-params new-match)]) 
    )
  )

(defn err [db [_ status]]
  (js/console.error "ERROR!" status)
  )

