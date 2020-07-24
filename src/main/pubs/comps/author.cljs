(ns pubs.comps.author
  (:require [pubs.comps.ui :as ui]
            [pubs.comps.option :as option]
            )
  )

(defn- get-name [v]
  (or (:name v) (:fullname v) (str (:firstname v) " " (:lastname v)))
  )

(defn- edit [v e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:authors/name v])
  (re-frame.core/dispatch [:panels/show :authors-new true])
  (re-frame.core/dispatch [:authors/edit false])
  )

(defn- rm [id e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:authors/rm id])
  )

(defn- poc [id e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:authors/poc id (-> e .-target .-checked)])
  )

(defn- options [s k v id]
  [:div.options-list.--as-panel {:class (if (get-in s [:ui :options k id]) :open) }
   [:div.inner
    (merge
      [:ul]
      (option/render s "#icon-edit" "Rename" (fn [s e] (edit v e)))
      (option/render s "#icon-delete" "Remove" (fn [s e] (rm id e))))
    ]
   ]
  )

(defn render [s k v id]
  [:li.item {:key id}
   (ui/icon s "#icon-user")
   [:div.main
    [:div.subject [:a {:href "#" :on-click #(edit v %) } (get-name v)]]
    [:div.meta [:a {:href "#" :on-click #(edit v %) } (:organization v)] ]
    [:div.ui.checkbox.inline.meta
     [:input {:type :checkbox
              :name :poc
              :onChange #(poc id %)
              :checked (get-in s [:data k id :poc])
              }]
     [:label (:for :poc) "Point of contact"]
     ]
    ]
   [:div.options {:on-click (fn [e]
                              (.preventDefault e)
                              (.stopPropagation e)
                              (re-frame.core/dispatch [:options/close])
                              (re-frame.core/dispatch [:options/show k id])
                              )}
    (ui/icon s "#icon-dots")
    (options s k v id)
    ]
   ]
  )

