(ns pubs.handlers.handler
  (:require [react :refer [useMemo]]
            [slate :refer [createEditor]]
            [slate-react :refer [withReact]]
            [reitit.frontend.controllers :as rfc]
            [pubs.hub :as hub])
  )

(defn start-controller [new-match]
  (->> (get-in new-match [:data :controllers])
       (first)
       (:start)
    )
  )

(defn navigated [db [_ new-match]]
  ;; This is not working for me :-/ - JBG
;  (let [old-match   (:current-route db)
;        controllers (rfc/apply-controllers (:controllers old-match) new-match)]
;    (assoc db :current-route (assoc new-match :controllers controllers)))

  (if-let [f (start-controller new-match)]
    (apply f [db (:path-params new-match)])
    db
    )
  )

(defn err [db [_ status]]
  (js/console.error "ERROR!" status)
  )


