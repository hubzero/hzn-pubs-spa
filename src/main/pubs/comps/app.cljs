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
;    [pubs.comps.options :as options] 
;    [pubs.comps.licenses :as licenses] 
;    [pubs.comps.citations :as citations] 
;    [pubs.comps.errors :as errors] 
;    [pubs.comps.help :as help] 
;    [pubs.comps.ui :as ui] 
;    [pubs.comps.summary :as summary] 
    [pubs.comps.breadcrumbs :as breadcrumbs]
    [pubs.comps.overlay :as overlay]
    [pubs.comps.panels.authors :as authors]
    [pubs.comps.panels.authors-new :as authors-new] 
    [pubs.comps.panels.citations-doi :as citations-doi]
    [pubs.comps.panels.citations-manual :as citations-manual]
    [pubs.comps.panels.files :as files]
    [pubs.comps.panels.licenses :as licenses]
    [pubs.comps.main :as main]
    [pubs.comps.summary :as summary]
    )
  )






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



;(defn handle-files-options [s e key]
;  (data/ls-files s)
;  (panels/show s e true key)
;  )
;

;(defn handle-citation-options [s e key]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (swap! s assoc-in [:ui :options :citations] true) 
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
    (files/render s :images)
    (files/render s :support-docs)
    (authors/render s :authors-list)
    (authors-new/render s :authors-new)
    (licenses/render s :licenses)
    (citations-doi/render s :citations-doi)
    (citations-manual/render s :citations-manual)
;    (help/help s :help-center)
    )
  )

