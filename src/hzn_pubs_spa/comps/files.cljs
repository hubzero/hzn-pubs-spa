(ns hzn-pubs-spa.comps.files
  (:require
    [hzn-pubs-spa.utils :as utils] 
    [hzn-pubs-spa.data :as data]
    [hzn-pubs-spa.comps.ui :as ui] 
    [hzn-pubs-spa.comps.panels :as panels] 
    [hzn-pubs-spa.comps.folders :as folders] 
    ) 
  )

(defn progress [s]
  [:div {:class [:ui :progress-bar]}
   [:div {:class :status}
    [:strong (str (get-in @s [:usage :size])
                  (get-in @s [:usage :units])
                  " ("
                  (get-in @s [:usage :percent])
                  "%)"
                  )]
    " of your "
    [:strong (str (get-in @s [:usage :max])
                  (get-in @s [:usage :units])
                  )]
    ]
   [:div {:class :progress}

    [:div {:class :bar
           :style {:right (str (- 100 (get-in @s [:usage :percent])) "%")}}
     [:span (str (get-in @s [:usage :percent]) "%")]
     ]
    ]
   ]
  )

(defn backarrow [s]
  [:a {:href "#"
       :class [:icon (if (> (count (get-in @s [:ui :current-folder])) 1) :show)]
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
    [:h1 (first (last (get-in @s [:ui :current-folder])))]
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

(defn- _add-file
  "state, file path, file name, key is type - :content, :images, :support-docs"
  [s path name key k]
  (prn "ADD FILE" path name key k)
  (data/add-file s {:type key
                    :index (count (get-in @s [:data key]))
                    :path (folders/spf path name)
                    :name name
                    })
  )

(defn- _rm-file
  "state, file path, file name, key is type - :content, :images, :support-docs, k is file id - JBG"
  [s path name key k]
  (prn "RM FILE" path name key k)
  (data/rm-file s key k)
  )

(defn file-click [s path n k e]
  (let [kk (utils/->keyword (folders/get-id s k path n))]
    (if (get-in @s [:data k kk])
      (_rm-file s path n k kk)
      (_add-file s path n k kk)
      )   
    )
  (data/usage s)
  )

(defn file [s path n k]
  [:li {:key n :on-click #(file-click s path n k %)}
   [:div.inner
    [:div.selected-indicator {:class (if (get-in @s [:data k
                                                     (utils/->keyword (folders/get-id s k path n)) 
                                                     ]) :selected)}
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

(defn file-selector [s files k index]
  [:ul.ui.file-selection.item-selector
   (select-all s k index)
   (doall (map (fn [[path n]] (file s path n k)) (as-> files $ (nth $ index) (map (fn [f] [(first $) f]) (last $)))))
   (doall (map (fn [[path n]] (folders/folder s path n k (inc index) subpanel)) (as-> files $ (nth $ index) (map (fn [f] [(first $) f]) (second $)))))
   ] 
  )

(defn subpanel [s files n k index]
  [:div.panel-subpanel.as-panel.files.-open {:id n}
   (file-selector s files k index)
   ] 
  )

(defn- _errors [s]
  [:div.overlay-panel-scroll-container
   [:div.inner
    [:p "No files in your project. :("]
    [:p "Please add files via your project to find them here."]
    ]
   ]
  )

(defn container [s files key index]
  [:div.overlay-panel-container
   (if (> (utils/file-count files) 0)
     (file-selector s files key index)
     (_errors s) 
     )
   ]
  )

(defn files [s k]
  [:div.page-panel.as-panel.-open {:class [k (if (get-in @s [:ui :panels k]) :open)]}
   [:div.inner
    (panels/header s "Add files from project")  
    (progress s)
    (subheader s)
    (container s (:files @s) k 0)
    ]
   ]
  )


