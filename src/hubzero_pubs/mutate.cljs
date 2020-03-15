(ns hubzero-pubs.mutate
  (:require [hubzero-pubs.utils :as utils]) 
  )

;(defn- _prepare-authors [data ak]
;  (->> (ak data)
;       (map-indexed (fn [i [k v]] [k (assoc v :index i)]))
;       (into {})
;       (assoc data ak)
;       )
;  )
;
;(defn- _prepare-files [data fk]
;  (->> (fk data)
;       (map (fn [[k v]] {:path k :name v}))
;       (assoc data fk)
;       )
;  )
;
(defn prepare [data]

;  (-> (_prepare-authors data :authors-list)
;      (_prepare-files :content)
;      (_prepare-files :images)
;      (_prepare-files :support-docs)
;      )
;
  (->
    (assoc data :license_type (:id (:licenses data)))
    ;(_prepare-authors :authors-list)
    )
  )

(defn- _coerce-authors [data ak]
  (->> (ak data)
       (sorted-map-by :index)
       (assoc data ak)
       )
  )

;(defn- _coerce-files [data fk]
;  (->> (fk data)
;       (reduce (fn [m f] (assoc m (:path f) (:name f))) {})
;       (assoc data fk)
;       )
;  )
;
(defn coerce [data]

  (-> (_coerce-authors data :authors-list)
;      (_coerce-files :content)
;      (_coerce-files :images)
;      (_coerce-files :support-docs)
      )
  )

(defn coerce-ui-state [s]
  (->
    (update @s :ui dissoc :current-panel) ;; This is a dom node reference which doesn't tavel well - JBG
    ) 
  )
