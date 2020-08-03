(ns pubs.handlers.licenses
  (:require [pubs.hub :as hub]
            [pubs.utils :as utils])
  )

(defn ack [db _]
  (update-in db [:data :ack] not)
  )

(defn select [db [_ k l]]
  (-> db
      (assoc-in [:ui k] (:id l))
      (assoc-in [:data k] l)
      )
  )

