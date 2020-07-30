(ns pubs.comps.collection
  (:require [react-sortablejs :refer [ReactSortable]]
            [pubs.comps.item :as item]
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
;  (if-let [l (into [] (get-in s [:data k]))]
    (order s k l)
    )
  )

(defn- selector-classes [s key classes]
  (concat classes (key {:authors-list [:options
                                       :author-selector
                                       (if (get-in s [:ui :options :authors]) :open)
                                       ]
                        :citations [:options
                                    :citations-selector
                                    (if (get-in s [:ui :options :citations]) :open)
                                    ]
                        }))
  )

(defn- selector-button [s k opts f]
  [:div {:class (selector-classes s k [:selector]) }
   [:a.selector-button {:href "#" :on-click #(f s k %)}
    (ui/icon s "#icon-plus")
    ] opts]
  )

(defn render [s id title k opts f]
  [:div.field.anchor.err {:id id :class (if (get-in s [:ui :errors k]) :with-error)}
   [:label {:for :title} title]
   [:div.collection
    (items s k)
    (selector-button s k opts f)
    ]
   (ui/val-error s k)
   ]
  )

