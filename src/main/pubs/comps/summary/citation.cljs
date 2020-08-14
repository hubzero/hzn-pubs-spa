(ns pubs.comps.summary.citation
  (:require [pubs.comps.ui :as ui]
            [pubs.utils :as utils]
            )
  )

(defn render [s c]
  [:li {:class :item :key (:id c)}
   [:div {:class :icon} (ui/icon s "#icon-file-text2") ]
   [:div {:class :main} (utils/format-citation c)]
   ] 
  )
 
