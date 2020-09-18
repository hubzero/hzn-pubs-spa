(ns pubs.comps.panels.databases
  (:require
    [pubs.comps.panels.header :as header] 
    [pubs.comps.ui :as ui] 
    [pubs.utils :as utils] 
    ) 
  )

(defn- add [s k db]
  (re-frame.core/dispatch [:dbs/add db])
  )

(defn- rm [s k id]
  (re-frame.core/dispatch [:dbs/rm k id])
  )

(defn- find-attachment [s k db]
  (->>
    (filter (fn [[_ a]]
              (= (:object_id a) (:id db))) (get-in s [:data k]))   
    (first)
    (second)
    )
  )

(defn- select? [s k db]
  (not (nil? (find-attachment s k db))) 
  )
 
(defn db-click [s k db e]
  (.preventDefault e)
  (.stopPropagation e)
  (if (select? s k db)
      (rm s k (:id (find-attachment s k db)))
      (add s k db)
      )
  )

(defn db [s k db]
  [:li {:key (:id db) :on-click #(db-click s k db %)}
   [:div.inner
    [:div.selected-indicator {:class (if (select? s k db) :selected)}
     [:div.icon
      (ui/icon s "#icon-checkmark")
      [:span.name "Selected"]
      ]
     ]
    [:div.icon
     (ui/icon s "#icon-data")
     [:span.name "Remove"]
     ] 
    (:title db) 
    ]
   ]
  )

(defn- db-sort [dbs]
  (sort-by #(clojure.string/lower-case (:title %)) dbs)
  )

(defn db-selector [s k dbs]
  [:ul.ui.file-selection.item-selector
   (doall (map #(db s k %) (db-sort dbs)))
   ]
  )

(defn- errors [s]
  [:div.overlay-panel-scroll-container
   [:div.inner
    [:p "No databases in your project. :("]
    [:p "Please add databases via your project to find them here."]
    ]
   ]
  )

(defn container [s k dbs]
  [:div.overlay-panel-container
   (if (> (count dbs) 0)
     (db-selector s k dbs)
     (errors s)
     )
   ]
  )

(defn render [s k]
  [:div.page-panel.as-panel.-open {:class [k (if (get-in s [:ui :panels k]) :open)]}
   [:div.inner
    (header/render s "Add databases from project")  
    (container s k (:databases s))
    ]
   ]
  )

