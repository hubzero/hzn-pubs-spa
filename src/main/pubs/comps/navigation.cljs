(ns pubs.comps.navigation

  )

(defn- handle-nav [id e]
  (.preventDefault e)
  (.stopPropagation e) 
  (-> (.getElementById js/document id)
      (.scrollIntoView (clj->js {:behavior :smooth}))
      )
  )

(defn item [id label & [header?]]
  [:li.item {:key id :class (if header? :header)}
   [:a {:href id :on-click #(handle-nav id %)} label]]
  )

(defn section [s]
  (merge 
    [:ul]
    (doall (map (fn [{id :id label :label header :header}]
                  (item id label header)
                  ) [{:id "a-essentials" :label "Essentials" :header true}
                     {:id "a-title" :label "Title"}
                     {:id "a-abstract" :label "Abstract"}
                     {:id "a-content" :label "Content"}
                     {:id "a-authors" :label "Authors"}
                     {:id "a-license" :label "License"}

                     {:id "a-details" :label "Additional Details" :header true}
                     {:id "a-image-gallery" :label "Image gallery"}
                     {:id "a-url" :label "External website URL"}
                     {:id "a-docs" :label "Supporting documents"}
                     {:id "a-tags" :label "Tags"}
                     {:id "a-citations" :label "Citations"}
                     {:id "a-version-notes" :label "Version release notes"}

                     {:id "a-pub-settings" :label "Publishing Settings" :header true}
                     {:id "a-pub-date" :label "Publication date"}
                     {:id "a-comments" :label "Comments to the administrator"}
                     ]))
    )
  )

(defn render [s]
  [^{:component-did-mount
     (fn []
       (js/StickySidebar. "#sidebar" (clj->js {:containerSelector "#aside"
                                               :innerWrapperSelector ".sidebar__inner"
                                               :topSpacing 0
                                               :bottomSpacing 0
                                               :minWidth 1000
                                               }))
       )
     }
   (fn []
     [:aside#aside
      [:nav#sidebar.internal.is-affixed
       [:div.sidebar__inner
        (section s)
        ]
       ]
      ]    
     )
   ]
  )
