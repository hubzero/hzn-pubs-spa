(ns pubs.comps.panel-header
  (:require
    [pubs.comps.ui :as ui] 
    ) 
  )

(defn- close [e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:panels/close])
  )

(defn render [s title]
  [:header
   [:a {:href "#" :class :icon :on-click #(close %)}
    (ui/icon s "#icon-left")
    [:span {:class :name} "Return"]
    ]
   [:div {:class :content}
    [:h1 title]
    ]
   ] 
  )

