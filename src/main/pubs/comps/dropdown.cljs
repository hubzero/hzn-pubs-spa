(ns pubs.comps.dropdown
  (:require
    [pubs.comps.ui :as ui] 
    )
  )

(defn- show [s k f e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:dropdown/show k f])
  )

(defn- change [s k f e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:dropdown/change k f (-> e .-target .-value)])
  )

(defn- click [s k f o e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:dropdown/click k f o])
  )

(defn- rm [s k f e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:dropdown/rm k f e])
  )

(defn render [s k f]
  (prn "DD" s k f)
  [:div.field.anchor.err {:key (:name f) :class (if (get-in s [:ui :errors (:name f)]) :with-error)}
   [:label {:for (:name f)} (str (:label f) ":")]
   [:div.proto-dropdown {:class (if (get-in s [:ui k (:name f)]) :open)}
    [:div.input-wrap
     [:input {:type :text
              :value (get-in s [:data k (:name f)])
              :onChange #(change s k f %)
              }]
     [:a.icon {:on-click #(rm s k f %)}
      (ui/icon s "#icon-cross")
      ]
     ]
    (merge
      [:ul.dropdown-menu.roll.listbox]    
      (doall (map (fn [o] [:li {:key o
                                :role :option
                                :on-click #(click s k f o %)
                                } o]) (:options f)))
      )
    [:a.icon {:href "#"
              :on-click #(show s k f %)
              } (ui/icon s "#icon-left")]
    ]
   (ui/val-error s (:name f))
   ]
  )
 
