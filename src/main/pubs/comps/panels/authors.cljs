(ns pubs.comps.panels.authors
  (:require
    [pubs.utils :as utils] 
    [pubs.comps.ui :as ui] 
    [pubs.comps.panels.header :as header] 
    )
  )

(defn- author? [s k id]
  (->
    (group-by :project_owner_id (vals (get-in s [:data k])))
    (get id)
    (first)
    )
  )

(defn user-click [s k u e]
    (if-let [a (author? s k (:id u))] 
      (re-frame.core/dispatch [:authors/rm (:id a)])
      (re-frame.core/dispatch [:authors/add u])
      )
  )

(defn user [s k u]
  [:li {:key (utils/author-key u)
        :on-click #(user-click s k u %)
        }
   [:div.inner
    [:div.selected-indicator {:class (if (author? s k (:id u)) :selected)}
     [:div.icon
      (ui/icon s "#icon-checkmark")
      [:span.name "Selected"]
      ]
     ]
    [:div.icon
     (ui/icon s "#icon-user")
     ] 
    (:fullname u)
    ]
   ]
  )

(defn users [s k]
  (merge
    [:ul.ui.user-selector.item-selector]
    (doall
      (map #(user s k %) (vals (:users s)))
      )
    )
  )

(defn render [s k]
  [:div.page-panel.as-panel {:class [k (if (get-in s [:ui :panels k]) :open)]}
   [:div.inner
    (header/render s "Add authors from project team")
    (users s k)
    ]
   ]
  )

