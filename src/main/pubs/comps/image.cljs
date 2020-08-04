(ns pubs.comps.image
  (:require [pubs.comps.ui :as ui]
            [pubs.comps.option :as option]
            )
  )

(defn- rm [s k id e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:req/rm-file k id])
  )

(defn- options [s k v id]
  [:div.options-list.--as-panel {:class (if (get-in s [:ui :options k id]) :open) }
   [:div.inner
    (merge
      [:ul]
      (option/render s "#icon-delete" "Remove" (fn [s e] (rm s k id e))))
    ]
   ]
  )

(defn render [s k v id]
  [:li.item {:key id}
   (ui/icon s "#icon-file-picture")
   [:div.main [:span (:name v)]]
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

