(ns pubs.comps.panels.field
  (:require [pubs.comps.ui :as ui])
  )

(defn- change [k n e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:panels/field k n (-> e .-target .-value)])
  )

(defn- textfield [s k n]
  [:div.field.anchor.err {:class (if (get-in s [:ui :errors (keyword n)]) :with-error)}
   [:input {:type :text
            :value (get-in s [:data k (keyword n)])
            :on-change #(change k (keyword n) %)}]
   (ui/val-error s (keyword n))
   ]
  )

(defn render [s k f]
  [:div {:class :field :key (:name f)}
   [:label {:for (:name f)} (:label f)]
   ((:type f) {
               :text (textfield s k (:name f))
               })
   ]
  )

