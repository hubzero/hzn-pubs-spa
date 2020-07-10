(ns pubs.handlers.res
  (:require [pubs.hub :as hub])
  )

(defn me [db [_ res]]
  (-> db
      (assoc :loading? false)
      (assoc-in [:data :user-id] (:id res))
      )
  )

(defn master-types [db [_ res]]
  (->> res 
       (filter #(some #{(:type %)} ["File(s)" "Databases" "Series"]))
       (assoc db :master-types)
       (hub/pub)
       )
  )

(defn- pub-master-type [db pub]
  (as-> (:master-types db) $
    (group-by :id $)
    ($ (:master-type pub))
    (first $)
    (:type $)
    (assoc pub :master-type {:master-type $})
    )
  )

(defn pub [db [_ res]]
  (let [pub (pub-master-type db res)]
    (-> (assoc db :data pub)
        (hub/prj)
      )
    )
  )

(defn prj [db [_ res]]
  (prn db)
  (assoc-in db [:data :prj] res)
  )

