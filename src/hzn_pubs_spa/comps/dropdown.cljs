(ns hzn-pubs-spa.comps.dropdown
  (:require
    [hzn-pubs-spa.comps.ui :as ui] 
    )
  )

(defn- _handle-dropdown [s k f e]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s update-in [:ui k (:name f)] not)
  )

(defn- _option-click [s k f o e]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s assoc-in [:data k (:name f)] o)
  (swap! s assoc-in [:ui k (:name f)] false)
  )

(defn- _option-rm [s k f e]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s assoc-in [:data k (:name f)] "")
  (swap! s assoc-in [:ui k (:name f)] false)
  )

(defn dropdown [s k f]
  [:div.field.anchor.err {:key (:name f) :class (if (get-in @s [:ui :errors (:name f)]) :with-error)}
   [:label {:for (:name f)} (str (:label f) ":")]
   [:div.proto-dropdown {:class (if (get-in @s [:ui k (:name f)]) :open)}
    [:div.input-wrap
     [:input {:type :text
              :value (get-in @s [:data k (:name f)])
              :onChange #(swap! s assoc-in [:data k (:name f)])
              }]
     [:a.icon {:on-click #(_option-rm s k f %)}
      (ui/icon s "#icon-cross")
      ]
     ]
    (merge
      [:ul.dropdown-menu.roll.listbox]    
      (doall (map (fn [o] [:li {:key o
                                :role :option
                                :on-click #(_option-click s k f o %)
                                } o]) (:options f)))
      )
    [:a.icon {:href "#"
              :on-click #(_handle-dropdown s k f %)
              } (ui/icon s "#icon-left")]
    ]
   (ui/val-error s (:name f))
   ]
  )
 
