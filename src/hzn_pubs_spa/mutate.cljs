(ns hzn-pubs-spa.mutate
  (:require [hzn-pubs-spa.utils :as utils]) 
  )

(defn- prepare-master-type [s]
  (as-> (:master-types @s) $
    (group-by :type $)
    ($ (get-in @s [:data :master-type :master-type])) 
    (first $)
    (:id $)
    ) 
  )

(defn prepare [s data]
  (assoc data
         :license_type (:id (:licenses data))
         ;; I don't love this ... but I want to re-use the dropdown
         :master-type (prepare-master-type s)
         )
  )

(defn- coerce-authors [data ak]
  (->> (ak data)
       (sorted-map-by :index)
       (assoc data ak)
       )
  )

(defn- coerce-master-type [s data]
  (as-> (:master-types @s) $
    (group-by :id $)
    ($ (:master-type data))
    (first $)
    (:type $)
    (assoc data :master-type {:master-type $})
    )
  )

(defn coerce [s data]
  (->>
    (coerce-authors data :authors-list)
    (coerce-master-type s)
    )
  )

(defn coerce-ui-state [s]
  (->
    ;; This is a dom node reference which doesn't tavel well - JBG
    (update @s :ui dissoc :current-panel) 
    ;; Going to npm components requires a toggle between interops - JBG
    clj->js 
    ) 
  )
