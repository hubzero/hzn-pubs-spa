(ns pubs.handlers.res
  (:require [pubs.hub :as hub]
            [pubs.utils :as utils]
            )
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
        (hub/files)
      )
    )
  )

(defn prj [db [_ res]]
  (assoc-in db [:data :prj] res)
  )

(defn files [db [_ res]]
  (update db :data merge res)
  )

(defn add-file [db [_ res file]]
  (prn "ADD FILE" res file)
  (as-> (:generated_key res) $
    (assoc-in db [:data (:type file) (utils/int->keyword $)] (assoc file :id $))
    )
  )

(defn rm-file [db [_ res k id]]
  (update-in db [:data k] dissoc (utils/int->keyword id))
  )

(defn ls-files [db [_ res]]
  (-> db
      (assoc :files res)
      (assoc-in [:ui :current-folder] [["Project files" (first (first res))]])
      )
  )

(defn usage [db [_ res]]
  (assoc db :usage res)
  )

