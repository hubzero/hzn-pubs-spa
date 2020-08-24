(ns pubs.comps.textfield
  (:require [pubs.comps.ui :as ui]
            [pubs.comps.help :as help]
            )
  )

(defn- change [s k e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:text/change k (-> e .-target .-value)])
  (re-frame.core/dispatch [:req/save-pub])
  )

(defn render [s id title k]
  [:div.field.anchor.err {:id id :class (if (get-in s [:ui :errors k]) :with-error)}
   [:label {:for :title} title (help/icon s)]
   [:input {:type :text
            :name (name k) 
            :value (get-in s [:data k])
            :on-change #(change s k %)
            }]
   (ui/val-error s k)
   ]
  )

