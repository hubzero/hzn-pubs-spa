(ns pubs.comps.files
  (:require
    [pubs.comps.folders :as folders] 
    [pubs.comps.panels :as panels] 
    [pubs.comps.ui :as ui] 
    [pubs.comps.usage :as usage] 
    [pubs.utils :as utils] 
    ;[hzn-pubs-spa.data :as data]
    ;[hzn-pubs-spa.comps.ui :as ui] 
    ) 
  )


(defn backarrow [s]
  [:a {:href "#"
       :class [:icon (if (> (count (get-in s [:ui :current-folder])) 1) :show)]
       :on-click #(folders/folder-pop s %)
       }
   (ui/icon s "#icon-left")
   [:span {:class :name} "Return"]
   ] 
  )

(defn subheader [s]
  [:header {:class :subheader}
   (backarrow s)   
   [:div {:class :content}
    [:h1 (first (last (get-in s [:ui :current-folder])))]
    ]  
   ]
  )

(defn select-all [s k index]
  [:li {:class :select-all :on-click #(folders/folder-click s k index %)}
   [:div {:class :inner}
    [:div {:class [:selected-indicator (if (folders/folder-selected? s k index) :selected)]}
     [:div {:class :icon}
      (ui/icon s "#icon-checkmark")
      [:span {:class :name} "Selected"]
      ]
     ] "Select all"
    ]
   ]
  )

(defn- add-file [s path n k]
  (re-frame.core/dispatch [:req/add-file {:type k
                                          :index (count (get-in s [:data k]))
                                          :path (folders/spf path n)
                                          :name n
                                          }])
  )

(defn- rm-file [s k id]
  (re-frame.core/dispatch [:req/rm-file k id])
  )

(defn file-click [s path n k e]
  (let [id (folders/get-id s k path n)]
    (if (get-in s [:data k id])
      (rm-file s k id)
      (add-file s path n k)
      )   
    )
  (re-frame.core/dispatch [:req/usage])
  )

(defn file [s path n k]
  [:li {:key n :on-click #(file-click s path n k %)}
   [:div.inner
    [:div.selected-indicator {:class (if (get-in s [:data k (folders/get-id s k path n)]) :selected)}
     [:div.icon
      (ui/icon s "#icon-checkmark")
      [:span.name "Selected"]
      ]
     ]
    [:div.icon
     (ui/icon s "#icon-file-text2")
     [:span.name "Remove"]
     ] 
    n
    ]
   ]
  )

(defn subpanel [s files n k index]
  [:div.panel-subpanel.as-panel.files.-open {:id n}
   (file-selector s files k index)
   ] 
  )
 
(defn file-selector [s files k index]
  [:ul.ui.file-selection.item-selector
   (select-all s k index)
   (doall (map (fn [[path n]] (file s path n k)) (as-> files $ (nth $ index) (map (fn [f] [(first $) f]) (last $)))))
   (doall (map (fn [[path n]] (folders/render s path n k (inc index) subpanel)) (as-> files $ (nth $ index) (map (fn [f] [(first $) f]) (second $)))))
   ]
  )

(defn- errors [s]
  [:div.overlay-panel-scroll-container
   [:div.inner
    [:p "No files in your project. :("]
    [:p "Please add files via your project to find them here."]
    ]
   ]
  )

(defn container [s files k index]
  [:div.overlay-panel-container
   (if (> (utils/file-count files) 0)
     (file-selector s files k index)
     (errors s) 
     )
   ]
  )

(defn render [s k]
  [:div.page-panel.as-panel.-open {:class [k (if (get-in s [:ui :panels k]) :open)]}
   [:div.inner
    (panels/header s "Add files from project")  
    (usage/render s)
    (subheader s)
    (container s (:files s) k 0)
    ]
   ]
  )


