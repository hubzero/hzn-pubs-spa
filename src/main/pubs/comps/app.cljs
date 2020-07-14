(ns pubs.comps.app
  (:require
    ;[secretary.core :as secretary]
    ;[react-sortablejs :refer [ReactSortable]]
    ;[hzn-pubs-spa.utils :as utils]
    ;[hzn-pubs-spa.data :as data]
    ;[hzn-pubs-spa.routes :as routes]
;    [pubs.comps.panels :as panels]
;    [pubs.comps.dropdown :as dropdown]
;    [pubs.comps.tags :as tags]
;    [pubs.comps.authors :as authors]
;    [pubs.comps.options :as options] 
;    [pubs.comps.licenses :as licenses] 
;    [pubs.comps.citations :as citations] 
;    [pubs.comps.errors :as errors] 
;    [pubs.comps.help :as help] 
;    [pubs.comps.ui :as ui] 
;    [pubs.comps.summary :as summary] 
    [pubs.comps.breadcrumbs :as breadcrumbs]
    [pubs.comps.files :as files]
    [pubs.comps.overlay :as overlay]
    [pubs.comps.main :as main]
    [pubs.comps.summary :as summary]
    )
  )





;(defn image [s k v id]
;  [:li.item {:key id}
;   (ui/icon s "#icon-file-picture")
;   [:div.main [:span (:name v)]]
;   [:div.options {:on-click (fn [e]
;                              (.preventDefault e)
;                              (.stopPropagation e)
;                              (options/close s)
;                              (swap! s assoc-in [:ui :options k id] true)
;                              )}
;    (ui/icon s "#icon-dots")
;    (options/items s k v id)
;    ]
;   ]
;  )
;
;(defn handle-poc-click [s e id]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (swap! s assoc-in [:data :authors-list id :poc] (-> e .-target .-checked))
;  (data/update-author s (get-in @s [:data :authors-list id]))
;  )
;
;
;(defn edit-author [s v e]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (swap! s assoc-in [:data :authors-new] (utils/fillname v))
;  (options/handle-author false s e)
;  )
;
;(defn- _get-name [v]
;  (or (:name v) (:fullname v) (str (:firstname v) " " (:lastname v)))
;  )
;
;(defn author [s k v id]
;  [:li.item {:key id}
;   (ui/icon s "#icon-user")
;   [:div.main
;    [:div.subject [:a {:href "#"
;                       :on-click #(edit-author s v %)
;                       } (_get-name v)]]
;    [:div.meta [:a {:href "#"
;                    :on-click #(edit-author s v %)
;                    } (:organization v)] ]
;    [:div.ui.checkbox.inline.meta
;     [:input {:type :checkbox
;              :name :poc
;              :on-change #(handle-poc-click s % id)
;              :checked (get-in @s [:data k id :poc])
;              }]
;     [:label (:for :poc) "Point of contact"]
;     ]
;    ]
;   [:div.options {:on-click (fn [e]
;                              (.preventDefault e)
;                              (.stopPropagation e)
;                              (options/close s)
;                              (swap! s assoc-in [:ui :options k id] true)
;                              )}
;    (ui/icon s "#icon-dots")
;    (options/items s k v id)
;    ]
;   ]
;  )
;
;
;(defn edit-citation [s c e]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (utils/water-citation s c)
;  (options/handle-manual s e)
;  )
;
;(defn citation [s k c]
;  [:li.item {:key (:id c)}
;   [:div.icon (ui/icon s "#icon-file-text2")]
;   [:div.main
;    [:div.subject
;     [:a {:href "#"
;          :on-click #(edit-citation s c %)
;          } (utils/format-citation c)]
;     ]
;    ]
;   [:div.options {:on-click (fn [e]
;                              (.preventDefault e)
;                              (.stopPropagation e)
;                              (options/close s)
;                              (swap! s assoc-in [:ui :options :citation (:id c)] true)
;                              )}
;    (ui/icon s "#icon-dots")
;    (options/citation s k c)
;    ]
;   ]
;  )
;
;(defn item [s k v]
;  ((k {
;       :content #(file s k (second v) (first v))
;       :support-docs #(file s k (second v) (first v))
;       :authors-list #(author s k (second v) (first v))
;       :images #(image s k (second v) (first v))
;       :citations #(citation s k (second v))
;       })) 
;  )
;

;(defn acknowledge [s]
;  [:div.details.last-child
;   [:div.inner
;    [:header "License acknowledgement"]
;    [:div.ui.checkbox.inline
;     [:input.important {:type :checkbox
;                        :name :ack
;                        :checked (or (get-in @s [:data :ack]) false) 
;                        :on-change #(swap! s update-in [:data :ack] not)
;                        } ]
;     [:label {:for :poc}
;      "I have read the "
;      [:a {:href (get-in @s [:data :licenses :url])
;           :target :_blank
;           } "license terms"]
;      " and agree to license my work under the attribution 3.0 unported license."
;      ]
;     ]
;    ]
;   ] 
;  )
;
;(defn license-item [s name detail]
;  [:div.item {:key name}
;   [:div.main
;    [:header.subject name]   
;    [:div.details.meta detail]  
;    ]
;   ]
;  )
;
;(defn handle-licenses-options [s e key]
;  (data/get-licenses s)
;  (panels/show s e true key)
;  )
;
;(defn licenses [s]
;  [:div.field.err {:class (if (or (get-in @s [:ui :errors :licenses])
;                                  (get-in @s [:ui :errors :ack])
;                                  ) 
;                            :with-error)}
;   [:label {:for :title} "License:"]
;   (merge
;     [:div.collection.single-item
;      [:div.item
;       [:div.main
;        [:header.subject
;         (get-in @s [:data :licenses :title])
;         ]
;        [:div.meta
;         (get-in @s [:data :licenses :info])
;         ]
;        ] 
;       ]
;      (if (get-in @s [:data :licenses]) (acknowledge s))
;      (selector-button s :licenses nil handle-licenses-options)
;      ]   
;     )
;   (ui/val-error s :licenses)
;   (ui/val-error s :ack)
;   ]
;  )
;
;(defn agreements [s]
;  [:div.field.err {:class (if (get-in @s [:ui :errors :terms]) :with-error)}
;   [:label {:for :agreement} "Agreements"]
;   [:div.field-wrapper
;    [:div.item.ui.checkbox.inline
;     [:input {:type :checkbox
;              :name :terms
;              :checked (or (get-in @s [:data :terms]) false) 
;              :on-change #(swap! s update-in [:data :terms] not)
;              }]
;     [:label {:for :terms} (:terms @s)]
;     ]
;    ]
;    (ui/val-error s :terms)
;   ]
;  )
;
;(defn handle-files-options [s e key]
;  (data/ls-files s)
;  (panels/show s e true key)
;  )
;
;(defn handle-author-options [s e k]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (swap! s assoc-in [:ui :options :authors] true) 
;  )
;

;(defn handle-citation-options [s e key]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (swap! s assoc-in [:ui :options :citations] true) 
;  )
;
;(defn additional-details [s]
;  [:fieldset.fieldset-section
;   [:header [:legend "Additional Details"]]
;   (collection s "a-image-gallery" "Image gallery:" :images nil handle-files-options)
;   (textfield s "a-url" "External website URL:" "url")
;   (collection s "a-docs" "Supporting docs:" :support-docs nil handle-files-options)
;   (collection s "a-citations" "Citations:" :citations (options/citations s) handle-citation-options)
;   (textarea s "a-verion-notes" "Version release notes:" "release-notes")
;   ]
;  )
;
;(defn pub-date [s]
;  [^{:component-did-mount
;     (fn []
;       (js/Lightpick. (clj->js {:field (.querySelector js/document "input[name=publication-date]")
;                                :onSelect (fn [date]
;                                            (swap! s assoc-in [:data :publication-date] (.format date "MM/DD/YYYY"))
;                                            )
;                                }))
;
;       (set! (.-value (.querySelector js/document "input[name=publication-date]")) (get-in @s [:data :publication-date]))
;       )
;     }
;   (fn []
;     [:div#a-pub-date.field.anchor.err {:class (if (get-in @s [:ui :errors :publication-date]) :with-error)}
;      [:label {:for :title} "Embargo date:"]
;      [:input {:type :text
;               :name "publication-date" 
;               }]
;      (ui/val-error s :publication-date)
;      ]
;     )
;   ]
;  )
;
;(defn publish-settings [s]
;  [:fieldset.fieldset-section
;   [:header#a-pub-settings.anchor.a-header [:legend "Publish Settings"]]
;   (pub-date s)
;   (textarea s "a-comments" "Comments to the administrator:" "comments")
;   ]
;  )
;
;(defn- _submit-draft [s e]
;  (when (not (utils/errors? s)) 
;    (.preventDefault e) 
;    (.stopPropagation e)  
;    (panels/show s e true :errors)     
;    )
;  )
;
;(defn aside-buttons [s]
;  [:aside
;   [:div.inner
;    [:fieldset.buttons-aside
;     ;;[:a.btn {:href "/pubs/#/summary"} "Proceed with the draft"]
;     ;;[:a.btn {:href "#" :on-click #(_submit-draft s %)} "Proceed with the draft"]
;     [:a.btn {:href (str "/pubs/#/pubs/"
;                         (get-in @s [:data :pub-id])
;                         "/v/"
;                         (get-in @s [:data :ver-id])
;                         )
;              :on-click #(_submit-draft s %)} "Proceed with the draft"]
;     [:a.btn.secondary {:href (str "/projects/"
;                                   (get-in @s [:data :prj-id])
;                                   "/publications"
;                                   )} "Save & Close"]
;     ;; https://localhost/projects/broodje/publications/274/continue
;     [:a.btn.secondary {:href (str "/projects/"
;                                   (get-in @s [:data :prj-id])
;                                   "/publications/"
;                                   (get-in @s [:data :pub-id])
;                                   "/continue"
;                                   )} "Switch to classic"]
;     ]
;    ]
;   ]
;  )
;
;(defn section-buttons [s]
;  [:fieldset.fieldset-section.buttons
;   [:div.field.buttons
;    [:a.btn {:href (str "/pubs/#/pubs/"
;                        (get-in @s [:data :pub-id])
;                        "/v/"
;                        (get-in @s [:data :ver-id])
;                        )
;             :on-click #(_submit-draft s %)} "Proceed with the draft"]
;    
;    ]
;   ]
;  )
;

(defn wrap [s]
  [:div.wrap {:on-click (fn [e]
                          ;(options/close s)
                          
                          ) }
   (breadcrumbs/render s)
   (if (get-in s [:ui :summary])
     (summary/render s) 
     (main/render s)
     ) 
   ])


;(defn- _save [s]
;  (if (utils/savable? s) (data/save-pub s))
;  )

(defn render [s]
  ;; Is this a hack, probably, save state and draft pub - JBG
  ;(_save s)
  (merge
    ;[:div {:on-click #(swap! s assoc-in [:ui :tag] false)}]
    [:div]
    (wrap s)
    (overlay/render s)
;    (errors/errors s :errors)
    (files/render s :content)
;    (files/files s :images)
;    (files/files s :support-docs)
;    (authors/authors-list s :authors-list)
;    (authors/authors-new s :authors-new)
;    (licenses/license-list s :licenses)
;    (citations/doi s :citations-doi)
;    (citations/manual s :citations-manual)
;    (help/help s :help-center)
    )
  )


