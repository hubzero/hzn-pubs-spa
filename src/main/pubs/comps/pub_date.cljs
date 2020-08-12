(ns pubs.comps.pub-date
  (:require [pubs.comps.ui :as ui])
  )

(defn- select [date]
  (re-frame.core/dispatch [:pub-date/select date])
  )

(defn render [s]
  [^{:component-did-mount
     (fn []
       (js/Lightpick. (clj->js {:field (.querySelector js/document "input[name=publication-date]")
                                :onSelect #(select %) 
                                }))

       (set! (.-value (.querySelector js/document "input[name=publication-date]")) (get-in s [:data :publication-date]))
       )
     }
   (fn []
     [:div#a-pub-date.field.anchor.err {:class (if (get-in s [:ui :errors :publication-date]) :with-error)}
      [:label {:for :title} "Embargo date:"]
      [:input {:type :text
               :name "publication-date" 
               }]
      (ui/val-error s :publication-date)
      ]
     )
   ]
  )

