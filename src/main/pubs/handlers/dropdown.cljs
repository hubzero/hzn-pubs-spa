(ns pubs.handlers.dropdown)

(defn show [db [_ k f e]]
  (update-in db [:ui k (:name f)] not)
  )

(defn change [db [_ k f v]]
  (assoc-in db [:data k (:name f)] v) 
  )

(defn click [db [_ k f o]]
  (-> db
      (assoc-in [:data k (:name f)] o)
      (assoc-in [:ui k (:name f)] false)
      )
  )

(defn rm [db [_ k f]]
  (-> db
      (assoc-in [:data k (:name f)] "")
      (assoc-in [:ui k (:name f)] false)      
      )
  )
 
