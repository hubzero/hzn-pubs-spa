(ns pubs.comps.summary.database
  (:require [pubs.comps.ui :as ui])
  )

(defn render [s db]
  [:li {:class :item :key (:id db)}
   [:div {:class :icon} (ui/icon s "#icon-data") ]
   [:div {:class :main} (:title db)]
   ]
  )
 
