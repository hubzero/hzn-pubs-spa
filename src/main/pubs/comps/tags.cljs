(ns pubs.comps.tags
  (:require ;[reagent.core :as r]
            [pubs.comps.ui :as ui]
            [pubs.comps.tag-creator :as tag-creator]
            [pubs.comps.tag-input :as tag-input]
            ;[hzn-pubs-spa.utils :as utils]
            )
  )

(defn rm [s t e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:tags/rm (:id t)])
  )

(defn tag [s t]
  [:a.ptag {:href (str "/tags/" (:raw_tag t))
            :key (:id t)
            :target :_blank
            }
   [:div.inner (:raw_tag t) 
    [:div.remove.icon
    {:on-click #(rm s t %)}
     (ui/icon s "#icon-cross")
     ]
    ]
   ]
  )

(defn- tags [s]
  (->> (get-in s [:data :tags])
       (vals) 
       (sort-by #(clojure.string/lower-case (:raw_tag %)))
       (map #(tag s %))
       )
  )

(defn render [s]
  [:div#a-tags.field.err {:class (if (get-in s [:ui :errors :tags]) :with-error)}
   [:label {:for :tags} "Tags:"]
   [:div.field-wrapper
    (merge
      [:div.item.ui.ptags]
      (tags s)
      (tag-creator/render s)
      (tag-input/render s)
      )
    ]
   (ui/val-error s :tags)
   ]
  )

