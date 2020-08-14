(ns pubs.comps.agreements
  (:require [pubs.comps.ui :as ui])
  )

(defn click [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:req/agree])
  )

(defn render [s]
  [:div.field.err {:class (if (get-in s [:ui :errors :terms]) :with-error)}
   [:label {:for :agreement} "Agreements"]
   [:div.field-wrapper
    [:div.item.ui.checkbox.inline
     [:input {:type :checkbox
              :name :terms
              :checked (or (get-in s [:data :terms]) false) 
              :onChange #(click s %)
              }]
     [:label {:for :terms} (:terms s)]
     ]
    ]
    (ui/val-error s :terms)
   ]
  )
 
