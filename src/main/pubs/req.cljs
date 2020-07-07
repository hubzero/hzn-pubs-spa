(ns pubs.req
  (:require [pubs.db :as db]
    [pubs.hub :as hub])
  )

(defn me [db [_ _]]
  (-> db
      (assoc :loading? true)
      (hub/me)
      )
  )

(defn master-types [db [_ res]]
  (hub/master-types db)
  )

(defn pub [db [_ params]]
  (-> db
      (assoc-in [:data :pub-id] (:pub-id params))
      (assoc-in [:data :ver-id] (:ver-id params))
      (hub/master-types)
      )
  )

