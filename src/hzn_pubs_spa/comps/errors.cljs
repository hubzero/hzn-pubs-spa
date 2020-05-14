(ns hzn-pubs-spa.comps.errors
  (:require
    [hzn-pubs-spa.comps.panels :as panels] 
    ) 
  )

(defn- _container [s]
  [:div.overlay-panel-scroll-container
   [:div.inner
    [:p "It seems like there are some errors you need address before submitting this draft:"]
    (merge
      [:ul]
      (doall (map (fn [[k v]]
                    [:li {:key k}
                     [:span {:class :error-field} (first v)] (str " " (second v) ".")
                     ]
                    ) (get-in @s [:ui :errors])))
      )
    ]
   ]
  )

(defn errors [s key]
  [:div.page-panel.as-panel.-open {:class [key (if (get-in @s [:ui :panels key]) :open)]}
   [:div.inner
    (panels/header s "Not quite...")  
    (_container s)
    ]
   ]
  )
