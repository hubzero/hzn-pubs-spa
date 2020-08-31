(ns pubs.comps.panels.folders
  (:require
    [pubs.comps.ui :as ui] 
    [pubs.utils :as utils] 
    ) 
  )

(defn spf [path file]
  (str path "/" file)
  )

(defn get-id [s k path file]
  (as-> (get-in s [:data k]) $
    (vals $)
    (group-by :path $)
    ($ (spf path file))
    (first $)
    (:id $)
    (str $)
    (keyword $)
    )
  )

(defn folder-pop [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (-> (get-in s [:ui :current-panel])
      (first)
      .-classList
      (.remove "open") 
      )

    (re-frame.core/dispatch [:panels/pop])
    (re-frame.core/dispatch [:folders/pop])
  )

(defn scroll-to-top []
  ;; HACK!!! - JBG
  (as-> js/document $
    (.querySelectorAll $ ".page-panel.open .inner")
    (doall (map #(.scrollTo % 0 0) $)) 
    )
  )

(defn folder-push [s n path e]
  (.preventDefault e)
  (.stopPropagation e)
  (let [node (-> e 
                 .-target
                 (utils/find-ancestor "li")
                 (.querySelector ".panel-subpanel")
                 )
        ]
    (if node (-> node .-classList (.toggle "open"))) 
    (re-frame.core/dispatch [:panels/push node])
    )
  (re-frame.core/dispatch [:folders/push [n path]])
  (scroll-to-top)
  )

(defn toggle-folder-files [s k index selected]
  (as-> (:files s) $
    (nth $ index) 
    (first $)
    (reduce (fn [c f]
              (if (clojure.string/includes? (first f) $)
                (reduce (fn [c2 f2]
                          (let [id (get-id s k (first f) f2)]
                            (if selected 
                              (re-frame.core/dispatch [:req/add-file {:type k
                                                                      :index 0 
                                                                      :path (spf (first f) f2)
                                                                      :name f2 
                                                                      }])
                              (re-frame.core/dispatch [:req/rm-file k id])
                              )
                            )
                          ) c (last f)) c)
              )
            (get-in s [:data k]) (:files s))  
    )
  )

(defn folder-click [s k index e]
  (.stopPropagation e)
  (let [classes (-> e 
                    .-target
                    (utils/find-ancestor "li")
                    (.querySelector ".selected-indicator")
                    .-classList
                    )]
    (toggle-folder-files s k index (not (boolean (some #{"selected"} (js/Array.from classes)))))
    (.toggle classes "selected")
    )
  )

(defn _folder-selected? [s k index]
  (as-> (:files s) $
    (nth $ index)
    (first $)
    (reduce (fn [c f]
              (if (clojure.string/includes? (first f) $)
                (and c
                     (reduce (fn [c2 f2]
                               (and c2 (boolean (get-in s [:data k
                                                           (utils/->keyword (get-id s k (first f) f2))
                                                           ])))
                               ) true (last f))
                     )
                c)
              )
            true (:files s))
    )
  )

(defn folder-selected? [s k index]
  (if (= (count (:files s)) 0) false (_folder-selected? s k index))
  )

(defn render [s path n k index subpanel]
  [:li {:key n :on-click #(folder-push s n path %)}
   [:div {:class [:inner :folder]}
    [:div {:class [:selected-indicator (if (folder-selected? s k index) :selected)] :on-click #(folder-click s k index %)}
     [:div {:class :icon }
      (ui/icon s "#icon-checkmark")
      [:span {:class :name} "Selected indicator"]
      ]
     ]
    [:header
     [:div {:class :icon}
      (ui/icon s "#icon-folder-open")
      [:span {:class :name} "Folder"]
      ]
     n
     ]
    ]
   (if (< index (count (:files s)))
     (subpanel s (:files s) n k index)
     )
   ]
  )

