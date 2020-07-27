(ns hzn-pubs-spa.comps.folders
  (:require
    [hzn-pubs-spa.utils :as utils] 
    [hzn-pubs-spa.data :as data] 
    [hzn-pubs-spa.comps.ui :as ui] 
    ) 
  )

(defn spf [path file]
  (str path "/" file)
  )

(defn get-id [s k path file]
  (as-> (get-in @s [:data k]) $
    (vals $)
    (group-by :path $)
    ($ (spf path file))
    (first $)
    (:id $)
    )
  )

(defn folder-pop [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (-> (get-in @s [:ui :current-panel])
      (first)
      .-classList
      (.remove "open") 
      )
  (swap! s update-in [:ui :current-panel] pop)
  (swap! s update-in [:ui :current-folder] pop)
  )

(defn folder-push [s name path e]
  (.preventDefault e)
  (.stopPropagation e)
  (when-let [node (-> e 
                 .-target
                 (utils/find-ancestor "li")
                 (.querySelector ".panel-subpanel")
                 )
        ]
    (-> node .-classList (.toggle "open"))
    (-> js/document (.querySelector (str ".page-panel.open .inner")) (.scrollTo 0 0))
    (swap! s update-in [:ui :current-panel] conj node)
    )
  (swap! s update-in [:ui :current-folder] conj [name path])
  )

(defn toggle-folder-files [s key index selected]
  (as-> (:files @s) $
    (nth $ index) 
    (first $)
    (reduce (fn [c f]
              (if (clojure.string/includes? (first f) $)
                (reduce (fn [c2 f2]
                          (let [k (get-id s key (first f) f2)]
                            (if selected 
                              (data/add-file s {:type key
                                                :index 0 
                                                :path (spf (first f) f2)
                                                :name f2 
                                                })
                              (data/rm-file s key k)
                              )
                            )
                          ) c (last f)) c)
              )
            (get-in @s [:data key]) (:files @s))  

    ;(swap! s assoc-in [:data key] $)
    )
  )

(defn folder-click [s key index e]
  (.stopPropagation e)
  (let [classes (-> e 
      .-target
      (utils/find-ancestor "li")
      (.querySelector ".selected-indicator")
      .-classList
      )]
      (toggle-folder-files s key index (not (boolean (some #{"selected"} (js/Array.from classes)))))
      (.toggle classes "selected")
    )
  )

(defn _folder-selected? [s key index]
  (as-> (:files @s) $
    (nth $ index) 
    (first $)
    (reduce (fn [c f]
              (if (clojure.string/includes? (first f) $)
                (and c 
                     (reduce (fn [c2 f2]
                               (and c2 (boolean (get-in @s [:data key (get-id s key (first f) f2)])))
                               ) true (last f))
                     )
                c)
              )
            true (:files @s))
    )
  )

(defn folder-selected? [s key index]
  (if (= (count (:files @s)) 0) false (_folder-selected? s key index))
  )

(defn folder [s path name key index subpanel]
  [:li {:key name :on-click #(folder-push s name path %)}
   [:div {:class [:inner :folder]}
    [:div {:class [:selected-indicator (if (folder-selected? s key index) :selected)] :on-click #(folder-click s key index %)}
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
     name
     ]
    ]
   (if (< index (count (:files @s)))
     (subpanel s (:files @s) name key index)
     )
   ]
  )

