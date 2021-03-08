(ns pubs.comps.panels.files
  (:require
    [pubs.comps.panels.folders :as folders] 
    [pubs.comps.panels.header :as header] 
    [pubs.comps.ui :as ui] 
    [pubs.comps.usage :as usage] 
    [pubs.utils :as utils] 
    ))

(defn click-back
  [e]
  (.stopPropagation e)                  ;; stop a href events
  (.preventDefault e)
  (re-frame.core/dispatch [:folders/pop]))

(defn backarrow [s]
  [:a {:href     "#"
       :class    [:icon (if (not= (last (get-in s [:files-m :location]))
                                  (get-in s [:files-m :root])) :show)]
       :on-click #(click-back %)
       }
   (ui/icon s "#icon-left")
   [:span {:class :name} "Return"]])

(defn subheader [s]
  [:header {:class :subheader}
   (backarrow s)   
   [:div {:class :content}
    [:h1
     (-> s
         (get-in [:files-m :location])
         (last)
         (clojure.string/split #"/")
         (last))]]])

(defn list-all-from
  "Returns a list of full filenames (including path) of all files at or below the
   currently specified directory location.

   files-map - The content of :files key from the :files-m file list state map.
   location  - A string representing the users current location in the file system."
  [files-map location]
  (let [{files :files subdirs :subdirs} (get files-map location)]
    (concat
      (map #(str location "/" %) files)
      (mapcat (partial list-all-from files-map)
           (map #(str location "/" %) subdirs)))))

(comment

  (list-all-from (:files (get-in @re-frame.db/app-db [:files-m]))
                 "sample/files")

  (get-in @re-frame.db/app-db [:data :images])
  (get-in @re-frame.db/app-db [:files-m])
  (last (get-in @re-frame.db/app-db [:files-m :location]))

  (clojure.set/subset?
    (into #{}
          (list-all-from (:files (get-in @re-frame.db/app-db [:files-m]))
                         "sample/files"))
    (into #{}
          (map :path (vals (get-in @re-frame.db/app-db [:data :images]))))

    )
  )

(defn click-select-all
  [db k]
  (let [selected (get-in db [:data k])
        location (-> db (get-in [:files-m :location]) last)
        all-at-loc (into #{} (list-all-from (-> db :files-m :files) location))
        all-sel (into #{} (map :path (-> db :data k vals)))
        diff (clojure.set/difference all-at-loc all-sel)]
    ;; at the current "location", get all files.
    ;; if every file in this list is in the selected files list, then DESELECT all
    ;; otherwise, SELECT all files not currently selected.
    (if (empty? diff)
      (do
        ;; Deselect all
        (doseq [id (keys selected)]
          (re-frame.core/dispatch [:req/rm-file k id])))
      (do
        ;; Select the difference
        (doseq [path diff]
          (re-frame.core/dispatch [:req/add-file {:type  k
                                                  :index 0
                                                  :path  path
                                                  :name  (-> path (clojure.string/split #"/") last)
                                                  }]))))))

(defn select-all
  ""
  [s k]
  [:li {:class :select-all :on-click #(click-select-all s k)}
   [:div {:class :inner}
    [:div {:class [:selected-indicator (if false :selected)]}
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

(defn file-m [s path k n]
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
    ]])

(defn file-selector-m
  [s {root :root filesm :files location :location} k]
  [:ul.ui.file-selection.item-selector
   (select-all s k)
   ;; TODO: Left off at Select All Implementaiton
   (let [current (last location)
         {subdirs :subdirs files :files} (get filesm current)]
     (concat
       (->> subdirs
            (sort-by #(clojure.string/lower-case %))
            (map (partial folders/render-m s current)))
       (->> files
            (sort-by #(clojure.string/lower-case %))
            (map (partial file-m s current k))))
     )])

(defn- errors [s]
  [:div.overlay-panel-scroll-container
   [:div.inner
    [:p "No files in your project. :("]
    [:p "Please add files via your project to find them here."]
    ]
   ]
  )

(defn container [s files k]
  [:div.overlay-panel-container
   (if (> (count (keys files)) 0)
     (file-selector-m s files k)
     (errors s) 
     )
   ]
  )

(defn render [s k]
  [:div.page-panel.as-panel.-open {:class [k (if (get-in s [:ui :panels k]) :open)]}
   [:div.inner
    (header/render s "Add files from project")  
    (usage/render s)
    (subheader s)
    (container s (:files-m s) k)
    ]
   ]
  )


