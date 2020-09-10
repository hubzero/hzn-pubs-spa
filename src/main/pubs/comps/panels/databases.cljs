(ns pubs.comps.panels.databases
  (:require
    [pubs.comps.panels.header :as header] 
    [pubs.comps.ui :as ui] 
    [pubs.utils :as utils] 
    ) 
  )

;(defn select-all [s k index]
;  [:li {:class :select-all :on-click #(folders/folder-click s k index %)}
;   [:div {:class :inner}
;    [:div {:class [:selected-indicator (if (folders/folder-selected? s k index) :selected)]}
;     [:div {:class :icon}
;      (ui/icon s "#icon-checkmark")
;      [:span {:class :name} "Selected"]
;      ]
;     ] "Select all"
;    ]
;   ]
;  )
;
(defn- add-db [s k db]
  (re-frame.core/dispatch [:req/add-db db])
  )

(defn- rm-db [s k id]
  (re-frame.core/dispatch [:req/rm-db k id])
  )

(defn db-click [s k db e]
  (.preventDefault e)
  (.stopPropagation e)
  (prn "DB CLICK" db)
  (if (get-in s [:data k (:id db)])
      (rm-db s k (:id db))
      (add-db s k db)
      )
  )

(defn db [s k db]
  [:li {:key (:name db) :on-click #(db-click s k db %)}
   [:div.inner
    [:div.selected-indicator {:class (if (get-in s [:data k (:id db)]) :selected) }
     [:div.icon
      (ui/icon s "#icon-checkmark")
      [:span.name "Selected"]
      ]
     ]
    [:div.icon
     (ui/icon s "#icon-file-text2")
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
   ;(select-all s k index)
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

