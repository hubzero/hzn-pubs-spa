(ns pubs.comps.selector-button
  (:require [pubs.comps.ui :as ui])
  )

(defn- classes [s k v]
  (concat v (k {:authors-list [:options
                               :author-selector
                               (if (get-in s [:ui :options :authors]) :open)
                               ]
                :citations [:options
                            :citations-selector
                            (if (get-in s [:ui :options :citations]) :open)
                            ]
                }))
  )

(defn render [s k opts f]
  [:div {:class (classes s k [:selector]) }
   [:a.selector-button {:href "#" :on-click #(f s k %)}
    (ui/icon s "#icon-plus")
    ] opts]
  )

