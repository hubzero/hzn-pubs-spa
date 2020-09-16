(ns pubs.handlers.series
  (:require [pubs.handlers.validate :as validate]
            [pubs.hub :as hub]
            [pubs.utils :as utils]
            )
  )

(defn search [db [_ v]]
  (as-> db $
      (if (> (count v) 0)
        (hub/search-series $ v)
        (dissoc $ :pubs)
        )
      (assoc $ :series-query v)
      ) 
  )

(defn series [db _]
  (hub/series db)
  )

(defn add [db [_ pub]]
  (hub/add-series db pub)
  )

(defn rm [db [_ k attach-id]]
  (hub/rm-series db k attach-id)
  )

(defn edit [db [_ k attach]]
  (hub/update-series db k attach)
  )

