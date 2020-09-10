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
  (re-frame.core/dispatch [:req/save-pub])
  )

(defn- click [s k f o e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:dropdown/click k f o])
  (re-frame.core/dispatch [:req/save-pub])
  )

(defn- rm [s k f e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:dropdown/rm k f e])
  (re-frame.core/dispatch [:req/save-pub])
  )

(defn- input [s k f]
  [:div.input-wrap
   [:input {:type :text
            :value (get-in s [:data k (:name f)])
            :onChange #(change s k f %)
            }]
   [:a.icon {:on-click #(rm s k f %)}
    (ui/icon s "#icon-cross")
    ]
   ]
  )

(defn- menu [s k f]
  (merge
    [:ul.dropdown-menu.roll.listbox]    
    (doall (map (fn [o] [:li {:key o
                              :role :option
                              :on-click #(click s k f o %)
                              } o]) (:options f)))
    )
  )

(defn- icon [s k f]
  [:a.icon {:href "#"
            :on-click #(show s k f %)
            } (ui/icon s "#icon-left")]
  )

(defn render [s k f]
  [:div.field.anchor.err {:key (:name f) :class (if (get-in s [:ui :errors (:name f)]) :with-error)}
   [:label {:for (:name f)} (str (:label f) ":")]
   [:div.proto-dropdown {:class (if (get-in s [:ui k (:name f)]) :open)}
    (input s k f) 
    (menu s k f)
    (icon s k f)
    ]
   (ui/val-error s (:name f))
   ]
  )

