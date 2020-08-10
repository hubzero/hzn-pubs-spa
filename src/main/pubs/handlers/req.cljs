(ns pubs.handlers.req
  (:require [pubs.hub :as hub]
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

(defn licenses [db _]
  (hub/licenses db)
  )

(defn- prepare-master-type [db]
  (as-> (:master-types db) $
    (group-by :type $)
    ($ (get-in db [:data :master-type :master-type]))
    (first $)
    (:id $)
    )
  )

(defn- prepare [db pub]
  (assoc pub 
         :license_type (:id (:licenses pub))
         :master-type (prepare-master-type db)
         )
  )

(defn save-pub [db _]
  (if (-> (get-in db [:data :state])
          (= 1)
          (not)
          )
    (->>
      (:data db)
      (prepare db)
      (hub/save-pub db)
      )
    db
    )
  )

(defn agree [db _]
  (update-in db [:data :terms] not)
  )

(defn rm-citation [db [_ id]]
  (hub/rm-citation db id)
  )

