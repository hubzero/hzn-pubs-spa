(ns pubs.comps.panels.licenses
  (:require
    [pubs.comps.ui :as ui] 
    [pubs.comps.panels.header :as header] 
    ) 
  )

(defn click [s l k e]
  ;(swap! s assoc-in [:ui key ] (:id l))
  ;(swap! s assoc-in [:data key] l)
  )

(defn item [s k l]
  [:div {:class :inner}
   [:div {:class [:selected-indicator (if (= (get-in s [:ui k]) (:id l)) :selected)]}
    [:div {:class :icon}
     (ui/icon s "#icon-checkmark")
     [:span {:class :name} "Selected"]
     ]
    ]
   [:div {:class :icon} (ui/icon s "#icon-file-text2")]
   [:div {:class :info} (:title l)]
   ]
  )

(defn- item-meta [s k l]
  [:div {:class [:meta (if (= (get-in s [:ui k]) (:id l)) :show)]}
   [:div {:class :inner}
    (:info l)
    ]
   ]
  )

(defn- license [s k l]
  [:li {:class :with-meta
        :key (:id l)
        :on-click #(click s l k %)}
   (item s k l)
   (item-meta s k l)
   ]
  )

(defn- suggest-link [s]
  [:li.with-meta
   [:div.inner
    [:p "Don't see a license in the list above that you would like to use? You can "
     [:a {:href (str "/projects/" (get-in s [:data :prj-id]) "/publications/" (get-in s [:data :pub-id]) "?action=suggest_license&version=1")} "suggest a license"]]  
    ]
   ]
  )

(defn- licenses [s k]
  (merge
    [:ul.ui.item-selector]
    (doall
      (map #(license s k %) (:licenses s))
      )
    (suggest-link s)
    )
  )

(defn render [s k]
  [:div {:class [:page-panel :as-panel k (if (get-in s [:ui :panels k]) :open)]}
   [:div {:class :inner}
    (header/render s "Select licenses")
    (licenses s k)
    ]
   ]
  )

