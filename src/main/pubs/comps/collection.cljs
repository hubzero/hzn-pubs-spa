(ns pubs.comps.collection
  (:require [react-sortablejs :refer [ReactSortable]]
            [pubs.comps.item :as item]
            [pubs.comps.selector-button :as sb]
            [pubs.comps.ui :as ui] 
            [pubs.utils :as utils]
            )
  )

(defn- reorder [s k l]
  (as-> l $
    (js->clj $)
    (map (fn [[k v]] [(keyword k) (utils/keywordize v)]) $)
    (into {} $) 
    (re-frame.core/dispatch [:collection/order k $])
    )
  )

(defn- order [s k l]
  [:> ReactSortable {:tag "ul" :list l :setList #(reorder s k %)}
   (doall
     (map #(item/render s k %) l)
     )
   ] 
  )

(defn items [s k]
  (if-let [l (sort-by #(:index (second %)) (into [] (get-in s [:data k])))]
    (order s k l)
    )
  )

(defn- files [s k e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:req/ls-files])
  (re-frame.core/dispatch [:req/usage])
  (re-frame.core/dispatch [:panels/show k true])
  )
 
(defn render [s id title k opts f]
  [:div.field.anchor.err {:id id :class (if (get-in s [:ui :errors k]) :with-error)}
   [:label {:for :title} title]
   [:div.collection
    (items s k)
    (sb/render s k opts f)
    ]
   (ui/val-error s k)
   ]
  )

