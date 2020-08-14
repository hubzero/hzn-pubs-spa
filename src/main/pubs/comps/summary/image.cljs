(ns pubs.comps.summary.image
  (:require [pubs.comps.ui :as ui])
  )

(defn render [s i]
  [:li {:class :item :key (:id i)}
   [:div {:class :icon} (ui/icon s "#icon-file-picture")]
   [:div {:class :main} (:name i)]
   ]
  )
 
