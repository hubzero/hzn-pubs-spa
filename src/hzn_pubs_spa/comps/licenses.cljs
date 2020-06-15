(ns hzn-pubs-spa.comps.licenses
  (:require
    [hzn-pubs-spa.utils :as utils] 
    [hzn-pubs-spa.comps.ui :as ui] 
    [hzn-pubs-spa.comps.panels :as panels] 
    ) 
  )

(defn license-click [s l key e]
  (swap! s assoc-in [:ui key ] (:id l))
  (swap! s assoc-in [:data key] l)
  )

(defn item [s key l]
  [:div {:class :inner}
   [:div {:class [:selected-indicator (if (= (get-in @s [:ui key]) (:id l)) :selected)]}
    [:div {:class :icon}
     (ui/icon s "#icon-checkmark")
     [:span {:class :name} "Selected"]
     ]
    ]
   [:div {:class :icon} (ui/icon s "#icon-file-text2")]
   [:div {:class :info} (:title l)]
   ]
  )

(defn item-meta [s key l]
  [:div {:class [:meta (if (= (get-in @s [:ui key]) (:id l)) :show)]}
   [:div {:class :inner}
    (:info l)
    ]
   ]
  )

(defn license [s key l]
  [:li {:class :with-meta :key (:id l) :on-click #(license-click s l key %)}
   (item s key l) 
   (item-meta s key l)
   ]
  )

(defn suggest-link [s]
  [:li.with-meta
   [:div.inner
    [:p "Don't see a license in the list above that you would like to use? You can "
     [:a {:href (str "/projects/" (get-in @s [:data :prj-id]) "/publications/" (get-in @s [:data :pub-id]) "?action=suggest_license&version=1")} "suggest a license"]]  
    ]
   ]
  )

(defn licenses [s k]
  (merge
    [:ul.ui.item-selector]
    (doall
      (map #(license s k %) (:licenses @s))
      )
    (suggest-link s)
    )
  )

(defn license-list [s key]
  [:div {:class [:page-panel :as-panel key (if (get-in @s [:ui :panels key]) :open)]}
   [:div {:class :inner}
    (panels/header s "Select licenses")
    (licenses s key)
    ]
   ]
  )

