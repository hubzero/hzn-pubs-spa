(ns pubs.handler
  (:require [reitit.frontend.controllers :as rfc]
            [pubs.db :as db]
            [pubs.hub :as hub])
  )

(defn initialize-db [_ _] db/default-db)

(defn navigated [db [_ new-match]]
  (let [old-match   (:current-route db)
        controllers (rfc/apply-controllers (:controllers old-match) new-match)]
    (assoc db :current-route (assoc new-match :controllers controllers)))
  )

(defn err [db [_ status]]
  (js/console.error "ERROR!" status)
  )

