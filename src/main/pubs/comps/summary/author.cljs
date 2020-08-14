(ns pubs.comps.summary.author
  (:require [pubs.comps.ui :as ui])
  )

(defn render [s a]
  [:li {:class :item :key (:id a )}
   [:div {:class :icon} (ui/icon s "#icon-user")]
   [:div {:class :main} 
    [:div {:class :subject} [:a {:href "#"} (:fullname a)]]
    [:div {:class :subject} [:a {:href "#"} (:organization a)]]
    ]
   ]
  )
 
