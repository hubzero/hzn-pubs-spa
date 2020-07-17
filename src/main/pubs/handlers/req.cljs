(ns pubs.handlers.req
  (:require [pubs.db :as db]
            [pubs.hub :as hub]
            [pubs.utils :as utils]
            )
  )

(defn me [db [_ _]]
  (-> db
      (assoc :loading? true)
      (hub/me)
      )
  )

(defn master-types [db _]
  (hub/master-types db)
  )

(defn pub [db [_ params]]
  (-> db
      (assoc-in [:data :pub-id] (:pub-id params))
      (assoc-in [:data :ver-id] (:ver-id params))
      (hub/master-types)
      )
  )

(defn prj [db _]
  (hub/prj db)
  )

(defn files [db _]
  (hub/files db)
  )

(defn ls-files [db _]
  (hub/ls-files db)
  )

(defn usage [db _]
  (->> (get-in db [:data :content] {})
       (vals)
       (map #(:path %))
       (hub/usage db)
       )
  )

(defn add-file [db [_ file]]
  (hub/add-file db file)
  )

(defn rm-file [db [_ k file-id]]
  (hub/rm-file db k (utils/->int file-id))
  )

(defn authors [db _]
  (hub/authors db)
  )

(defn owners [db _]
  (hub/owners db)
  )

