(ns pubs.comps.citation
  (:require [pubs.comps.ui :as ui]
            [pubs.comps.option :as option]
            [pubs.utils :as utils]
            )
  )

(defn- rm [s id e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:req/rm-citation id])
  )

(defn- edit [s c e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:options/close])
  (re-frame.core/dispatch [:citations/edit c])
  (re-frame.core/dispatch [:panels/show :citations-manual true])
  )

(defn click [s c e]
  (re-frame.core/dispatch [:options/close])
  (re-frame.core/dispatch [:citations/options (:id c)]) 
  )

(defn options [s k c]
  [:div.options-list.--as-panel {:class (if (get-in s [:ui :options :citation (:id c)]) :open) }
   [:div.inner
    (merge
      [:ul]
      (option/render s "#icon-edit" "Edit" (fn [s e] (edit s c e)))
      (option/render s "#icon-delete" "Remove" (fn [s e] (rm s (:id c) e))))
    ]
   ]
  )

(defn render [s k c]
  [:li.item {:key (:id c)}
   [:div.icon (ui/icon s "#icon-file-text2")]
   [:div.main
    [:div.subject
     [:a {:href "#"
          :on-click #(edit s c %)
          } (utils/format-citation c)]
     ]
    ]
   [:div.options { :on-click #(click s c %) }
    (ui/icon s "#icon-dots")
    (options s k c)
    ]
   ]
  )
 
