(ns pubs.comps.summary.series
  (:require [pubs.comps.ui :as ui])
  )

(defn render [s series]
  [:li {:class :item :key (:id series)}
   [:div {:class :icon} (ui/icon s "#icon-file-text2") ]
   [:div {:class :main} (:title series)]
   ] 
  )
 
