(ns hubzero-pubs.comps.folders
  (:require
    [hubzero-pubs.utils :as utils] 
    [hubzero-pubs.data :as data] 
    [hubzero-pubs.comps.ui :as ui] 
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
  (let [node (-> e 
                 .-target
                 (utils/find-ancestor "li")
                 (.querySelector ".panel-subpanel")
                 )
        ]
    (-> node .-classList (.toggle "open"))
    (swap! s update-in [:ui :current-panel] conj node)
    )
  (swap! s update-in [:ui :current-folder] conj [name path])
  )

(defn toggle-folder-files [s key index selected]
  (as-> (get-in @s [:ui :files]) $
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
            (get-in @s [:data key]) (get-in @s [:ui :files]))  

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
  (as-> (get-in @s [:ui :files]) $
    (nth $ index) 
    (first $)
    (reduce (fn [c f]
              (if (clojure.string/includes? (first f) $)
                (and c
                     (reduce (fn [x y] (and x y)) (reduce (fn [c2 f2]
                                                            (and c2 (get-in @s [:data key (get-id s key (first f) f2)]))
                                                            ) c (last f)))
                     )

                c)
              )
            true (get-in @s [:ui :files])) 
    )
  )

(defn folder-selected? [s key index]
  (if (= (count (get-in @s [:ui :files])) 0) false (_folder-selected? s key index))
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
   (if (< index (count (get-in @s [:ui :files])))
     (subpanel s (get-in @s [:ui :files]) name key index)
     )
   ]
  )

