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
        (hub/authors)
        (hub/tags)
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
  (as-> (:generated_key res) $
    (assoc-in db [:data (:type file) (utils/->keyword $)] (assoc file :id $))
    )
  )

(defn rm-file [db [_ res k id]]
  (update-in db [:data k] dissoc (utils/->keyword id))
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

(defn authors [db [_ res]]
  (assoc-in db [:data :authors-list] res)
  )

(defn owners [db [_ res]]
  (->> res
       (map (fn [u] [(:id u) u]))
       (into {})
       (assoc db :users)
       )
  )

(defn rm-author [db [_ res id]]
  (update-in db [:data :authors-list] dissoc (utils/->keyword id))
  )

(defn update-author [db [_ res]]
  (assoc-in db [:data :authors-list (utils/->keyword (:id res))] res)
  )

(defn add-author [db [_ res]]
  (assoc-in db [:data :authors-list (utils/->keyword (:id res))] res)
  )

(defn new-author [db [_ res]]
  (assoc-in db [:data :authors-list (utils/->keyword (:id res))] res)
  )

(defn search-users [db [_ res]]
  (assoc db :user-results res)
  )

(defn tags [db [_ res]]
  (->>
    res
    (group-by :id)
    (map (fn [[k v]] [k (first v)]))
    (into {})
    (assoc-in db [:data :tags])
    )
  )

(defn rm-tag [db _]
  (hub/tags db)
  )

(defn add-tag [db _]
  (-> db
      (assoc-in [:ui :tag-str] "")
      (dissoc :tag-query)
      (assoc-in [:ui :tag] false)
      (hub/tags)
      ) 
  )

(defn search-tags [db [_ res]]
  (assoc db :tag-results res) 
  )

