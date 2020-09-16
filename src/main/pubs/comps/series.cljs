(ns pubs.comps.series
  (:require [pubs.comps.ui :as ui]
            [pubs.comps.option :as option]
            )
  )

(defn- rm [s k id e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:series/rm k id])
  )

(defn- options [s k v]
  [:div.options-list.--as-panel {:class (if (get-in s [:ui :options k (:id v)]) :open) }
   [:div.inner
    (merge
      [:ul]
      (option/render s "#icon-delete" "Remove" (fn [s e] (rm s k (:id v) e))))
    ]
   ]
  )

(defn render [s k v]
  [:li.item {:key (:id v)}
   (ui/icon s "#icon-file-text2")
   [:div.main [:span (:title v)]]
   [:div.options {:on-click (fn [e]
                              (.preventDefault e)
                              (.stopPropagation e)
                              (re-frame.core/dispatch [:options/close])
                              (re-frame.core/dispatch [:options/show k (:id v)])
                              )}
    (ui/icon s "#icon-dots")
    (options s k v)
    ]
   ] 
  )

