(ns hzn-pubs-spa.comps.app
  (:require
    [secretary.core :as secretary]
    [react-sortablejs :refer [ReactSortable]]
    [hzn-pubs-spa.utils :as utils]
    [hzn-pubs-spa.data :as data]
    [hzn-pubs-spa.routes :as routes]
    [hzn-pubs-spa.comps.panels :as panels]
    [hzn-pubs-spa.comps.files :as files]
    [hzn-pubs-spa.comps.dropdown :as dropdown]
    [hzn-pubs-spa.comps.tags :as tags]
    [hzn-pubs-spa.comps.authors :as authors]
    [hzn-pubs-spa.comps.options :as options] 
    [hzn-pubs-spa.comps.licenses :as licenses] 
    [hzn-pubs-spa.comps.citations :as citations] 
    [hzn-pubs-spa.comps.errors :as errors] 
    [hzn-pubs-spa.comps.help :as help] 
    [hzn-pubs-spa.comps.ui :as ui] 
    [hzn-pubs-spa.comps.summary :as summary] 
    )
  )

(defn- _handle-value [e s name]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s assoc-in [:data (keyword name)] (-> e .-target .-value))
  )

(defn- _help [s]
  [:a.icon {:href "#" :on-click (fn [e]
                                  (.preventDefault e)
                                  (.stopPropagation e)
                                  (panels/show s e true :help-center)
                                  )}
   (ui/icon s "#icon-question")
   ]
  )


(defn textfield [s id title name]
  [:div.field.anchor.err {:id id :class (if (get-in @s [:ui :errors (keyword name)]) :with-error)}
   [:label {:for :title} title
    (_help s)
    ]
   [:input {:type :text
            :name name
            :value (get-in @s [:data (keyword name)])
            :on-change #(_handle-value % s name)
            }]
   (ui/val-error s (keyword name))
   ]
  )

(defn textarea [s id title name]
  [:div.field {:id id :class (if (get-in @s [:ui :errors (keyword name)]) :with-error)}
   [:label {:for :title} title]
   [:textarea {:name name
               :value (get-in @s [:data (keyword name)])
               :on-change #(_handle-value % s name)
               }]
   (ui/val-error s (keyword name))
   ]
  )

(defn file [s k v id]
  [:li.item {:key id}
   (ui/icon s "#icon-file-text2")
   [:div.main [:span (:name v)]]
   [:div.options {:on-click (fn [e]
                              (.preventDefault e)
                              (.stopPropagation e)
                              (options/close s)
                              (swap! s assoc-in [:ui :options k id] true)
                              )}
    (ui/icon s "#icon-dots")
    (options/items s k v id)
    ]
   ] 
  )

(defn image [s k v id]
  [:li.item {:key id}
   (ui/icon s "#icon-file-picture")
   [:div.main [:span (:name v)]]
   [:div.options {:on-click (fn [e]
                              (.preventDefault e)
                              (.stopPropagation e)
                              (options/close s)
                              (swap! s assoc-in [:ui :options k id] true)
                              )}
    (ui/icon s "#icon-dots")
    (options/items s k v id)
    ]
   ]
  )

(defn handle-poc-click [s e id]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s assoc-in [:data :authors-list id :poc] (-> e .-target .-checked))
  (data/update-author s (get-in @s [:data :authors-list id]))
  )


(defn edit-author [s v e]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s assoc-in [:data :authors-new] (utils/fillname v))
  (options/handle-author false s e)
  )

(defn- _get-name [v]
  (or (:name v) (:fullname v) (str (:firstname v) " " (:lastname v)))
  )

(defn author [s k v id]
  [:li.item {:key id}
   (ui/icon s "#icon-user")
   [:div.main
    [:div.subject [:a {:href "#"
                       :on-click #(edit-author s v %)
                       } (_get-name v)]]
    [:div.meta [:a {:href "#"
                    :on-click #(edit-author s v %)
                    } (:organization v)] ]
    [:div.ui.checkbox.inline.meta
     [:input {:type :checkbox
              :name :poc
              :on-change #(handle-poc-click s % id)
              :checked (get-in @s [:data k id :poc])
              }]
     [:label (:for :poc) "Point of contact"]
     ]
    ]
   [:div.options {:on-click (fn [e]
                              (.preventDefault e)
                              (.stopPropagation e)
                              (options/close s)
                              (swap! s assoc-in [:ui :options k id] true)
                              )}
    (ui/icon s "#icon-dots")
    (options/items s k v id)
    ]
   ]
  )


(defn edit-citation [s c e]
  (.preventDefault e)
  (.stopPropagation e)
  (utils/water-citation s c)
  (options/handle-manual s e)
  )

(defn citation [s k c]
  [:li.item {:key (:id c)}
   [:div.icon (ui/icon s "#icon-file-text2")]
   [:div.main
    [:div.subject
     [:a {:href "#"
          :on-click #(edit-citation s c %)
          } (utils/format-citation c)]
     ]
    ]
   [:div.options {:on-click (fn [e]
                              (.preventDefault e)
                              (.stopPropagation e)
                              (options/close s)
                              (swap! s assoc-in [:ui :options :citation (:id c)] true)
                              )}
    (ui/icon s "#icon-dots")
    (options/citation s k c)
    ]
   ]
  )

(defn item [s k v]
  ((k {
       :content #(file s k (second v) (first v))
       :support-docs #(file s k (second v) (first v))
       :authors-list #(author s k (second v) (first v))
       :images #(image s k (second v) (first v))
       :citations #(citation s k (second v))
       })) 
  )

(defn- _set-list [s k l]
  (->> l 
       (js->clj)
       (map (fn [[k v]] [k (utils/keywordize v)]))
       (into {})
       (swap! s assoc-in [:data k])
       )
  ;; Update all the author indexes/sort order - JBG
  (doall
    (case k
      :authors-list
      (map-indexed (fn [i a] (data/update-author s 
                                                 (assoc a :index i)
                                                 )) (vals (get-in @s [:data k])))
      (prn "SKIP UPDATE")
      )
    )
  )

(defn- _sortitems [s k l]
  [:> ReactSortable {:tag "ul" :list l :setList #(_set-list s k %)}
   (doall
     (map #(item s k %) l)
     )
   ] 
  )

(defn items [s k]
  (if-let [l (into [] (get-in @s [:data k]))]
    (_sortitems s k l)
    )
  )

(defn selector-classes [s key classes]
  (concat classes (key {:authors-list [:options
                                       :author-selector
                                       (if (get-in @s [:ui :options :authors]) :open)
                                       ]
                        :citations [:options
                                    :citations-selector
                                    (if (get-in @s [:ui :options :citations]) :open)
                                    ]
                        }))
  )

(defn selector-button [s key options-comp f]
  [:div {:class (selector-classes s key [:selector]) }
   [:a.selector-button {:href "#" :on-click #(f s % key)}
    (ui/icon s "#icon-plus")
    ] options-comp]
  )

(defn collection [s id title k options-comp f]
  [:div.field.anchor.err {:id id :class (if (get-in @s [:ui :errors k]) :with-error)}
   [:label {:for :title} title]
   [:div.collection
    (items s k)
    (selector-button s k options-comp f)
    ]
   (ui/val-error s k)
   ]
  )

(defn acknowledge [s]
  [:div.details.last-child
   [:div.inner
    [:header "License acknowledgement"]
    [:div.ui.checkbox.inline
     [:input.important {:type :checkbox
                        :name :ack
                        :checked (or (get-in @s [:data :ack]) false) 
                        :on-change #(swap! s update-in [:data :ack] not)
                        } ]
     [:label {:for :poc}
      "I have read the "
      [:a {:href (get-in @s [:data :licenses :url])
           :target :_blank
           } "license terms"]
      " and agree to license my work under the attribution 3.0 unported license."
      ]
     ]
    ]
   ] 
  )

(defn license-item [s name detail]
  [:div.item {:key name}
   [:div.main
    [:header.subject name]   
    [:div.details.meta detail]  
    ]
   ]
  )

(defn handle-licenses-options [s e key]
  (data/get-licenses s)
  (panels/show s e true key)
  )

(defn licenses [s]
  [:div.field.err {:class (if (or (get-in @s [:ui :errors :licenses])
                                  (get-in @s [:ui :errors :ack])
                                  ) 
                            :with-error)}
   [:label {:for :title} "License:"]
   (merge
     [:div.collection.single-item
      [:div.item
       [:div.main
        [:header.subject
         (get-in @s [:data :licenses :title])
         ]
        [:div.meta
         (get-in @s [:data :licenses :info])
         ]
        ] 
       ]
      (if (get-in @s [:data :licenses]) (acknowledge s))
      (selector-button s :licenses nil handle-licenses-options)
      ]   
     )
   (ui/val-error s :licenses)
   (ui/val-error s :ack)
   ]
  )

(defn agreements [s]
  [:div.field.err {:class (if (get-in @s [:ui :errors :terms]) :with-error)}
   [:label {:for :agreement} "Agreements"]
   [:div.field-wrapper
    [:div.item.ui.checkbox.inline
     [:input {:type :checkbox
              :name :terms
              :checked (or (get-in @s [:data :terms]) false) 
              :on-change #(swap! s update-in [:data :terms] not)
              }]
     [:label {:for :terms} (:terms @s)]
     ]
    ]
    (ui/val-error s :terms)
   ]
  )

(defn handle-files-options [s e key]
  (data/ls-files s)
  (panels/show s e true key)
  )

(defn handle-author-options [s e k]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s assoc-in [:ui :options :authors] true) 
  )

(defn master-types [s]
  (dropdown/dropdown s :master-type {:name :master-type
                                     :label "Master Type"
                                     :options (map #(:type %) (:master-types @s))
                                     })
  )

(defn essentials [s]
  [:fieldset.fieldset-section
   [:header
    [:legend "Essentials"]
    [:div.note "all fields required"]
    ]
   (master-types s)
   (textfield s "a-title" "Title:" "title")
   (textarea s "a-abstract" "Abstract:" "abstract")
   (collection s "a-content" "Content:" :content nil handle-files-options)
   (collection s "a-authors" "Authors (drag to reorder):" :authors-list (options/authors s) handle-author-options)
   (tags/tags s)
   (licenses s)
   (agreements s)
   ]
  )

(defn handle-citation-options [s e key]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s assoc-in [:ui :options :citations] true) 
  )

(defn additional-details [s]
  [:fieldset.fieldset-section
   [:header [:legend "Additional Details"]]
   (collection s "a-image-gallery" "Image gallery:" :images nil handle-files-options)
   (textfield s "a-url" "External website URL:" "url")
   (collection s "a-docs" "Supporting docs:" :support-docs nil handle-files-options)
   (collection s "a-citations" "Citations:" :citations (options/citations s) handle-citation-options)
   (textarea s "a-verion-notes" "Version release notes:" "release-notes")
   ]
  )

(defn pub-date [s]
  [^{:component-did-mount
     (fn []
       (js/Lightpick. (clj->js {:field (.querySelector js/document "input[name=publication-date]")
                                :onSelect (fn [date]
                                            (swap! s assoc-in [:data :publication-date] (.format date "MM/DD/YYYY"))
                                            )
                                }))

       (set! (.-value (.querySelector js/document "input[name=publication-date]")) (get-in @s [:data :publication-date]))
       )
     }
   (fn []
     [:div#a-pub-date.field.anchor.err {:class (if (get-in @s [:ui :errors :publication-date]) :with-error)}
      [:label {:for :title} "Embargo date:"]
      [:input {:type :text
               :name "publication-date" 
               }]
      (ui/val-error s :publication-date)
      ]
     )
   ]
  )

(defn publish-settings [s]
  [:fieldset.fieldset-section
   [:header#a-pub-settings.anchor.a-header [:legend "Publish Settings"]]
   (pub-date s)
   (textarea s "a-comments" "Comments to the administrator:" "comments")
   ]
  )

(defn- _submit-draft [s e]
  (when (not (utils/errors? s)) 
    (.preventDefault e) 
    (.stopPropagation e)  
    (panels/show s e true :errors)     
    )
  )

(defn aside-buttons [s]
  [:aside
   [:div.inner
    [:fieldset.buttons-aside
     ;;[:a.btn {:href "/pubs/#/summary"} "Proceed with the draft"]
     ;;[:a.btn {:href "#" :on-click #(_submit-draft s %)} "Proceed with the draft"]
     [:a.btn {:href (str "/pubs/#/pubs/"
                         (get-in @s [:data :pub-id])
                         "/v/"
                         (get-in @s [:data :ver-id])
                         )
              :on-click #(_submit-draft s %)} "Proceed with the draft"]
     [:a.btn.secondary {:href (str "/projects/"
                                   (get-in @s [:data :prj-id])
                                   "/publications"
                                   )} "Save & Close"]
     ;; https://localhost/projects/broodje/publications/274/continue
     [:a.btn.secondary {:href (str "/projects/"
                                   (get-in @s [:data :prj-id])
                                   "/publications/"
                                   (get-in @s [:data :pub-id])
                                   "/continue"
                                   )} "Switch to classic"]
     ]
    ]
   ]
  )

(defn section-buttons [s]
  [:fieldset.fieldset-section.buttons
   [:div.field.buttons
    [:a.btn {:href (str "/pubs/#/pubs/"
                        (get-in @s [:data :pub-id])
                        "/v/"
                        (get-in @s [:data :ver-id])
                        )
             :on-click #(_submit-draft s %)} "Proceed with the draft"]
    
    ]
   ]
  )

(defn main-form [s]
  [:main
   [:form
    (essentials s) 
    (additional-details s)
    (publish-settings s)
    (section-buttons s)
    ]
   ]
  )

(defn- _handle-nav [id e]
  (.preventDefault e)
  (.stopPropagation e) 
  (-> (.getElementById js/document id)
      (.scrollIntoView (clj->js {:behavior :smooth}))
      )
  )

(defn- _menu-item [id label & [header?]]
  [:li.item {:key id :class (if header? :header)}
   [:a {:href id :on-click #(_handle-nav id %)} label]]
  )

(defn nav-section [s]
  (merge 
    [:ul]
    (doall (map (fn [{id :id label :label header :header}]
                  (_menu-item id label header)
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

(defn navigation [s]
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
        (nav-section s)
        ]
       ]
      ]    
     )
   ]
  )

(defn master-type [s]
  [:span.restype.indlist[:span.dataset "dataset"]]
  )

(defn- _breadcrumbs [s]
  [:div
   [:h3.publications.c-header
    [:span [:a {:href (str "/projects/" (get-in @s [:data :prj :alias]))} "Projects"]]
    " / "
    [:span [:a {:href (str
                        "/projects/"
                        (get-in @s [:data :prj :alias]))

                } (get-in @s [:data :prj :title])]]
    " / "
    [:span [:a {:href (str
                        "/projects/"
                        (get-in @s [:data :prj :alias])
                        "/publications"
                        )} "Publications"]]
    " / "
    [:span [:a {:href (str
                        "/projects/"
                        (get-in @s [:data :prj :alias])
                        "/publications/"       
                        (get-in @s [:data :pub-id])
                        )
                } (if (> (count (get-in @s [:data :title])) 0) (get-in @s [:data :title]) "Untitled")]]
    " » "
    (master-type s)
    ]
   ]
  )

(defn page-main [s]
  [:div {:class (concat [:page :page-main :--remove]
                        (if (get-in @s [:ui :summary])
                          [:hide :remove]
                          )
                        ) }
   (navigation s) 
   (main-form s)
   (aside-buttons s)
   ]
  )

(defn wrap [s]
  [:div.wrap {:on-click (fn [e] (options/close s)) }
   (_breadcrumbs s)
   (if (get-in @s [:ui :summary])
     (summary/page-summary s) 
     (page-main s)
     ) 
   ])


(defn- _save [s]
  (if (utils/savable? s) (data/save-pub s))
  )

(defn- _app [s]
  ;; Is this a hack, probably, save state and draft pub - JBG
  (_save s)
  (merge
    [:div {:on-click #(swap! s assoc-in [:ui :tag] false)}]
    (wrap s)
    (panels/overlay s)
    (errors/errors s :errors)
    (files/files s :content)
    (files/files s :images)
    (files/files s :support-docs)
    (authors/authors-list s :authors-list)
    (authors/authors-new s :authors-new)
    (licenses/license-list s :licenses)
    (citations/doi s :citations-doi)
    (citations/manual s :citations-manual)
    (help/help s :help-center)
    )
  )

(defn app [s]
  (if (get-in @s [:data :pub-id]) (_app s))
  )

