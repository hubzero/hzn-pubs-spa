(ns pubs.comps.citation
  (:require 
    
    [pubs.comps.ui :as ui]
            
            )  
  )

(defn- rm [s id e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:req/rm-citation id])
  )

(defn- click [s c e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:options/close])
  ;(swap! s assoc-in [:ui :options :citation (:id c)] true)
  )

(defn options [s k c]
  [:div.options-list.--as-panel {:class (if (get-in s [:ui :options :citation (:id c)]) :open) }
   [:div.inner
    (merge
      [:ul]
      (item s "#icon-edit" "Edit" #(click s c %))
      (item s "#icon-delete" "Remove" #(rm s (:id c) %)))
    ]
   ]
  )

(defn render [s k c]
  [:li.item {:key (:id c)}
   [:div.icon (ui/icon s "#icon-file-text2")]
   [:div.main
    [:div.subject
     [:a {:href "#"
          ;:on-click #(edit-citation s c %)
          } (utils/format-citation c)]
     ]
    ]
   [:div.options { :on-click #(click s c %) }
    (ui/icon s "#icon-dots")
    (options s k c)
    ]
   ]
  )
 
