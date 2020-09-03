(ns pubs.comps.panels.authors-new  
  (:require
    [pubs.utils :as utils] 
    [pubs.comps.ui :as ui] 
    [pubs.comps.panels.field :as field] 
    [pubs.comps.panels.header :as header] 
    )
  )

(defn add-click [s k e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:authors/upsert k])
  (re-frame.core/dispatch [:panels/close])
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

