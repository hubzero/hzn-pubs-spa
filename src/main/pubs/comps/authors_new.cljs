(ns pubs.comps.authors-new  
  (:require
    [pubs.utils :as utils] 
    ;[pubs.data :as data] 
    [pubs.comps.ui :as ui] 
    [pubs.comps.panel-field :as field] 
    [pubs.comps.panel-header :as header] 
    )
  )

;(defn- author? [s k id]
;  (->
;    (group-by :project_owner_id (vals (get-in s [:data k])))
;    (get id)
;    (first)
;    )
;  )
;
;(defn user-click [s k u e]
;    (if-let [a (author? s k (:id u))] 
;      (re-frame.core/dispatch [:authors/rm (:id a)])
;      (re-frame.core/dispatch [:authors/add u])
;      )
;  )
;
;(defn user [s k u]
;  [:li {:key (utils/author-key u)
;        :on-click #(user-click s k u %)
;        }
;   [:div.inner
;    [:div.selected-indicator {:class (if (author? s k (:id u)) :selected)}
;     [:div.icon
;      (ui/icon s "#icon-checkmark")
;      [:span.name "Selected"]
;      ]
;     ]
;    [:div.icon
;     (ui/icon s "#icon-user")
;     ] 
;    (:fullname u)
;    ]
;   ]
;  )
;
;(defn users [s k]
;  (merge
;    [:ul.ui.user-selector.item-selector]
;    (doall
;      (map #(user s k %) (vals (:users s)))
;      )
;    )
;  )
;
;(defn render [s k]
;  [:div.page-panel.as-panel {:class [k (if (get-in s [:ui :panels k]) :open)]}
;   [:div.inner
;    (panels/header s "Add authors from project team")
;    (users s k)
;    ]
;   ]
;  )
;
;(defn- _handle-author [s k e]
;  (let [u (get-in @s [:data k])
;        u (assoc u :fullname (str (:firstname u) " " (:lastname u)))
;        ]
;    (if (get-in @s [:ui :author-options :is-new]) 
;      (data/new-author s u)
;      (data/update-author s u)
;      ) 
;    (panels/close s e)
;    ;; Clear form - JBG
;    (swap! s update :data dissoc k)
;    ;; Scroll form, am I a dirty hack? ... yes. - JBG
;    (-> js/document (.querySelector (str "." (name k) " .inner")) (.scrollTo 0 0))    
;    ) 
;  )

(defn add-click [s k e]
  (.preventDefault e)
  (.stopPropagation e)

  ;  (if (utils/authors-new-valid? s)
  ;    (_handle-author s k e)
  ;    ) 

  (re-frame.core/dispatch [:authors/upsert k])

  )

(defn close [e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:panels/close])
  )

(defn buttons [s k]
  [:div.field.buttons
   [:a.btn {:href "#"
            :on-click #(add-click s k %)
            }  (if (get-in s [:ui :author-options :is-new]) "Add author" "Save author")]
   [:a.btn.secondary {:href "#"
                      :on-click #(close %)
                      } "Close"]
   ]
  )

(defn- handle-result [s k v e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:authors/result k v])
  )

(defn result [s k v]
  [:li.result {:key (str (:name v) (:org v))}
   [:a {:href "#"
        :on-click #(handle-result s k v %)
        } (:name v) ", " [:span (:org v)]]
   ]
  )

(defn results [s k]
  (merge [:ul.results]
         (doall (map #(result s k %) (:user-results s)))
         )
  )

(defn- handle-search [s v e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:authors/search v])
  )

(defn- search [s k]
  [:div.field
   [:label {:for :title} "Look up author:"]
   [:input.form-textinput.loading-circle.hide-loading-circle {:type :text
                                                              :value (:user-query s)
                                                              :onChange #(handle-search s (-> % .-target .-value) %)
                                                              }]
   [:div.ui.autocomplete
    (results s k)
    ]
   ]
  )

(defn- fieldset [s k fields] 
  [:fieldset {:class k}
   (when (get-in s [:ui :author-options :is-new])
     [:div (search s k)
      [:hr]
      ]
     )
   (merge
     [:div.selected-item]
     (doall (map #(field/render s k %) fields))
     )
   (buttons s k)
   ]
  )

(defn render [s k]
  [:div.page-panel.as-panel {:class [k (if (get-in s [:ui :panels k]) :open)]}
   [:div.inner
    (if (get-in s [:ui :author-options :is-new])
      (header/render s "Add new authors")
      (header/render s "Edit author information")
      )
    (fieldset s k [{:name "firstname" :label "First name" :type :text}
                   {:name "lastname" :label "Last name" :type :text}
                   {:name "organization" :label "Organization" :type :text}
                   {:name "email" :label "Email" :type :text}
                   ])
    ]
   ]
  )

