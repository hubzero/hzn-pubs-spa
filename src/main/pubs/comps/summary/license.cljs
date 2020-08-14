(ns pubs.comps.summary.license
  (:require [pubs.comps.summary.ack :as ack])
  )

(defn render [s l]
  [:div {:class [:collection :single-item :collection-summary]}
   [:div {:class :item}
    [:div {:class :main}
     [:header {:class :subject} (:title l)]
     [:div {:class :meta} (:info l)]
     ]
    ]
   (ack/render s)
   ]
  )
 
