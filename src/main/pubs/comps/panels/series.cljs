(ns pubs.comps.panels.series
  (:require
    [pubs.comps.panels.header :as header] 
    [pubs.comps.ui :as ui] 
    [pubs.utils :as utils] 
    ) 
  )

(defn- add-pub [s k pub]
  (re-frame.core/dispatch [:series/add pub])
  )

(defn- rm-pub [s k id]
  (re-frame.core/dispatch [:series/rm k id])
  )

(defn- find-attachment [s k pub]
  (->>
    (filter (fn [[_ a]]
              (= (:object_id a) (:id pub))) (get-in s [:data k]))   
    (first)
    (second)
    )
  )

(defn- select? [s k pub]
  (not (nil? (find-attachment s k pub))) 
  )
 
(defn pub-click [s k pub e]
  (.preventDefault e)
  (.stopPropagation e)
  (if (select? s k pub)
      (rm-pub s k (:id (find-attachment s k pub)))
      (add-pub s k pub)
      )
  )

(defn pub [s k pub]
  [:li {:key (:id pub) :on-click #(pub-click s k pub %)}
   [:div.inner
    [:div.selected-indicator {:class (if (select? s k pub) :selected)}
     [:div.icon
      (ui/icon s "#icon-checkmark")
      [:span.name "Selected"]
      ]
     ]
    [:div.icon
     (ui/icon s "#icon-file-text2")
     [:span.name "Remove"]
     ] 
    [:div.group
        [:p (:title pub)]
        [:p (:doi pub)] 
     ]
    ]
   ]
  )

(defn- pub-sort [pubs]
  (sort-by #(clojure.string/lower-case (:title %)) pubs)
  )

(defn pub-selector [s k pubs]
  [:ul.ui.file-selection.item-selector
   (doall (map #(pub s k %) (pub-sort pubs)))
   ]
  )

(defn- errors [s]
  [:div.overlay-panel-scroll-container
   [:div.inner
    [:p "No publications :("]
    ]
   ]
  )

(defn- search [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:series/search (-> e .-target .-value)])
  )

(defn search-field [s k]
  [:div.field
   [:label {:for :series-query} "Search Publications (Title, DOI, etc.):"]
   [:input {:type :text
            :value (:series-query s)
            :onChange #(search s %)
            :placeholder "Search..."
            }
    ]
   ]
  )

(defn container [s k pubs]
  [:div.overlay-panel-container
   (search-field s k)
   (if (> (count pubs) 0)
     (pub-selector s k pubs)
     ;(errors s)
     )
   ]
  )

(defn render [s k]
  [:div.page-panel.as-panel.-open {:class [k (if (get-in s [:ui :panels k]) :open)]}
   [:div.inner
    (header/render s "Add publications to the series")  
    (container s k (:pubs s))
    ]
   ]
  )

