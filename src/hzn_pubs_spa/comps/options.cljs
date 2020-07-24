(ns hzn-pubs-spa.comps.options
  (:require
    [hzn-pubs-spa.utils :as utils]
    [hzn-pubs-spa.data :as data]
    [hzn-pubs-spa.comps.panels :as panels]
    )
  )

(defn close [s]
  (swap! s assoc-in [:ui :options] nil)
  )

(defn handle-author [is-new s e]
   (.preventDefault e)
   (.stopPropagation e)
   (panels/show-overlay s true)
   (swap! s assoc-in [:ui :panels :authors-new] true)
   (swap! s assoc-in [:ui :author-options :is-new] is-new)
   (close s) ;; why?
  )
 
(defn item [s i name f]
  [:li
   [:a {:href "#" :on-click #(f s %)}
    [:div {:class :icon}
     [:svg [:use {:xlinkHref i}]]
     [:span.name name]
     ] name]
   ]
  )

(defn- _remove [s e k id]
  (.preventDefault e)
  (.stopPropagation e)
;  (swap! s assoc-in [:data k] (dissoc (get-in @s [:data k]) id))
  (case k
    :content (data/rm-file s k id)
    :images (data/rm-file s k id)
    :support-docs (data/rm-file s k id)
    :authors-list (data/rm-author s id)
    )
  )

(defn- _edit-author [e s v]
  (.preventDefault e) 
  (.stopPropagation e)
  (swap! s assoc-in [:data :authors-new] (utils/fillname v))
  (handle-author false s e)
  )

(defn items [s k v id]
  [:div.options-list.--as-panel {:class (if (get-in @s [:ui :options k id]) :open) }
   [:div.inner
    (merge
      [:ul]
      (if (= k :authors-list)
        (item s "#icon-edit" "Rename" (fn [s e] (_edit-author e s v)))
        )
      ;(item s "#icon-download" "Download" #())
      (item s "#icon-delete" "Remove" (fn [s e]
                                        (_remove s e k id)
                                        )))
    ]
   ]
  )

(defn handle-manual [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (data/get-citation-types s)
  (panels/show-overlay s true)
  (swap! s assoc-in [:ui :panels :citations-manual] true)
  (close s)
  )

(defn- _edit-citation [e s c]
  (.preventDefault e)
  (.stopPropagation e)
  (utils/water-citation s c)
  (handle-manual s e)
  )

(defn citation [s k c]
  [:div.options-list.--as-panel {:class (if (get-in @s [:ui :options :citation (:id c)]) :open) }
   [:div.inner
    (merge
      [:ul]
      (item s "#icon-edit" "Edit" (fn [s e] (_edit-citation e s c)))
      (item s "#icon-delete" "Remove" (fn [s e]
                                        (.preventDefault e)
                                        (.stopPropagation e)
                                        (prn c)
                                        (data/rm-citation s (:id c))
                                        )))
    ]
   ]
  )


(defn handle-add-author [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (data/get-owners s)
  (panels/show-overlay s true)
  (swap! s assoc-in [:ui :panels :authors-list] true)
  (close s)
  )

(defn handle-new-author [s e]
  (swap! s assoc-in [:data :authors-new] nil)
  (handle-author true s e)
  )

(defn authors [s]
  [:div.authors-options.options-list {:class (if (get-in @s [:ui :options :authors]) :open)}
   [:div.inner
    (merge
      [:ul]
      (item s "#icon-user" "Add from project" handle-add-author)
      (item s "#icon-user" "Add new" handle-new-author)
      )
    ]
   ]
  )

(defn handle-doi [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (panels/show-overlay s true)
  (swap! s assoc-in [:ui :panels :citations-doi] true)
  (close s)
  )

(defn citations [s]
  [:div.citations-options.options-list {:class (if (get-in @s [:ui :options :citations]) :open)}
   [:div.inner
    (merge
      [:ul]
      (item s "#icon-file-text2" "Enter a DOI" handle-doi)
      (item s "#icon-file-text2" "Enter Manually" handle-manual)
      )
    ]
   ]
  )
