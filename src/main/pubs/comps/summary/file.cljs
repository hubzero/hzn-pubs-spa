(ns pubs.comps.summary.file
  (:require [pubs.comps.ui :as ui])
  )

(defn render [s f]
  [:li {:class :item :key (:id f)}
   [:div {:class :icon} (ui/icon s "#icon-file-text2") ]
   [:div {:class :main} (:name f)]
   ] 
  )
 
