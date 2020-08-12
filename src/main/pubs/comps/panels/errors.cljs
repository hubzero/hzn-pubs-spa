(ns pubs.comps.panels.errors
  (:require [pubs.comps.panels.header :as header]) 
  )

(defn- error [s [k v]]
  [:li {:key k}
   [:span {:class :error-field} (first v)] (str " " (second v) ".")
   ]
  )

(defn- errors [s]
  [:div.overlay-panel-scroll-container
   [:div.inner
    [:p "It seems like there are some errors you need address before submitting this draft:"]
    (merge
      [:ul]
      (doall (map #(error s %) (get-in s [:ui :errors])))
      )
    ]
   ]
  )

(defn render [s k]
  [:div.page-panel.as-panel.-open {:class [k (if (get-in s [:ui :panels k]) :open)]}
   [:div.inner
    (header/render s "Not quite...")  
    (errors s)
    ]
   ]
  )

