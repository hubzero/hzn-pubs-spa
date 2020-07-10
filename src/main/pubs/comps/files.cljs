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

(defn select-all [s key index]
  [:li {:class :select-all :on-click #(folders/folder-click s key index %)}
   [:div {:class :inner}
    [:div {:class [:selected-indicator (if (folders/folder-selected? s key index) :selected)]}
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

(defn file-click [s path name key e]
  (let [k (folders/get-id s key path name)]
    (if (get-in @s [:data key k])
      (_rm-file s path name key k)
      (_add-file s path name key k)
      )   
    )
  (data/usage s)
  )

(defn file [s path name key]
  [:li {:key name :on-click #(file-click s path name key %)}
   [:div.inner
    [:div.selected-indicator {:class (if (get-in @s [:data key (folders/get-id s key path name)]) :selected)}
     [:div.icon
      (ui/icon s "#icon-checkmark")
      [:span.name "Selected"]
      ]
     ]
    [:div.icon
     (ui/icon s "#icon-file-text2")
     [:span.name "Remove"]
     ] 
    name
    ]
   ]
  )

(defn file-selector [s files key index]
  [:ul.ui.file-selection.item-selector
   (select-all s key index)
   (doall (map (fn [[path name]] (file s path name key)) (as-> files $ (nth $ index) (map (fn [f] [(first $) f]) (last $)))))
   (doall (map (fn [[path name]] (folders/folder s path name key (inc index) subpanel)) (as-> files $ (nth $ index) (map (fn [f] [(first $) f]) (second $)))))
   ] 
  )

(defn subpanel [s files name key index]
  [:div.panel-subpanel.as-panel.files.-open {:id name}
   (file-selector s files key index)
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

(defn files [s key]
  [:div.page-panel.as-panel.-open {:class [key (if (get-in @s [:ui :panels key]) :open)]}
   [:div.inner
    (panels/header s "Add files from project")  
    (progress s)
    (subheader s)
    (container s (:files @s) key 0)
    ]
   ]
  )


