(ns hubzero-pubs.mutate
  (:require [hubzero-pubs.utils :as utils]) 
  )

(defn prepare [data]
  (assoc data :license_type (:id (:licenses data)))
  )

(defn- _coerce-authors [data ak]
  (->> (ak data)
       (sorted-map-by :index)
       (assoc data ak)
       )
  )

(defn coerce [data]
  (_coerce-authors data :authors-list)
  )

(defn coerce-ui-state [s]
  (->
    (update @s :ui dissoc :current-panel) ;; This is a dom node reference which doesn't tavel well - JBG
    ) 
  )
