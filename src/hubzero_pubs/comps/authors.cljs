(ns hubzero-pubs.comps.authors
  (:require
    [hubzero-pubs.utils :as utils] 
    [hubzero-pubs.data :as data] 
    [hubzero-pubs.comps.ui :as ui] 
    [hubzero-pubs.comps.panels :as panels] 
    ) 
  )

(defn user-click [s u key e]
  (swap! s update-in [:ui key (utils/author-key u)] not)
  (if (get-in @s [:data key (utils/author-key u)])
    (swap! s update-in [:data key] dissoc (utils/author-key u))
    (data/add-author s u)
    )
  )

(defn user [s key u]
  [:li {:key (utils/author-key u) :on-click #(user-click s u key %)}
   [:div.inner
    [:div.selected-indicator {:class (if (get-in @s [:ui key (utils/author-key u)]) :selected)}
     [:div.icon
      (ui/icon s "#icon-checkmark")
      [:span.name "Selected"]
      ]
     ]
    [:div.icon
     (ui/icon s "#icon-user")
     ] 
    (:fullname u)
    ]
   ]
  )

(defn users [s key]
  (merge
    [:ul.ui.user-selector.item-selector]
    (doall
      (map #(user s key %) (vals (:users @s)))
      )
    )
  )

(defn authors-list [s key]
  [:div.page-panel.as-panel {:class [key (if (get-in @s [:ui :panels key]) :open)]}
   [:div.inner
    (panels/header s "Add authors from project team")
    (users s key)
    ]
   ]
  )

(defn add-click [s k e]
  (.preventDefault e)
  (.stopPropagation e)
  (let [u (get-in @s [:data k])]
    (prn "ADDDDD USER" u)
    (if (get-in @s [:ui :author-options :is-new]) 
      (data/new-author s (assoc u
                                :fullname (str (:firstname u) " " (:lastname u))
                                :id (:id u 0)
                                ))
      (data/update-author s u)
      )   
    )

  (panels/close s e)
  ;; Clear form - JBG
  (swap! s update :data dissoc k)
  ;; Scroll form, am I a dirty hack? ... yes. - JBG
  (-> js/document (.querySelector (str "." (name k) " .inner")) (.scrollTo 0 0))
  )

(defn authors-buttons [s k]
  [:div.field.buttons
   [:a.btn {:href "#"
            :on-click #(add-click s k %)
            }  (if (get-in @s [:ui :author-options :is-new]) "Add author" "Save author")]
   [:a.btn.secondary {:href "#"
                      :on-click #(panels/close s %)
                      } "Close"]
   ]
  )

(defn result-click [s k e res]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s assoc-in [:data k] {:firstname (str (:givenname res) " " (:middlename res))
                               :lastname (:surname res)
                               :organization (:org res)
                               :email (:email res)
                               :id (:id res 0)
                               :name (:name res)
                               })
  (swap! s assoc :user-query "")
  (swap! s assoc :user-results nil)
  )

(defn result [s key res]
  [:li.result {:key (str (:name res) (:org res))}
   [:a {:href "#"
        :on-click #(result-click s key % res)
        } (:name res) ", " [:span (:org res)]]
   ]
  )

(defn results [s key]
  (merge [:ul.results]
         (doall (map #(result s key %) (:user-results @s)))
         )
  )

(defn- _search [s v]
  (swap! s assoc :user-query v)
  (if (not (empty? v))
    (data/search-users s) 
    )
  )

(defn search [s key]
  [:div.field
   [:label {:for :title} "Look up author:"]
   [:input.form-textinput.loading-circle.hide-loading-circle {:type :text
                                                              :value (:user-query @s)
                                                              :onChange #(_search s (-> % .-target .-value))
                                                              }]
   [:div.ui.autocomplete
    (results s key)
    ]
   ]
  )

(defn fieldset [s key fields] 
  [:fieldset {:class key}
   (when (get-in @s [:ui :author-options :is-new])
     [:div (search s key)
      [:hr]
      ]
     )
   (merge
     [:div.selected-item]
     (doall (map #(panels/field s key %) fields))
     )
   (authors-buttons s key)
   ]
  )

(defn authors-new [s key]
  [:div.page-panel.as-panel {:class [key (if (get-in @s [:ui :panels key]) :open)]}
   [:div.inner
    (if (get-in @s [:ui :author-options :is-new])
      (panels/header s "Add new authors")
      (panels/header s "Edit author information")
      )
    (fieldset s key [{:name "firstname" :label "First name" :type :text}
                     {:name "lastname" :label "Last name" :type :text}
                     {:name "organization" :label "Organization" :type :text}
                     {:name "email" :label "Email" :type :text}
                     ])
    ]
   ]
  )



