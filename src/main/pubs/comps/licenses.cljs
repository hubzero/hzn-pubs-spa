(ns pubs.comps.licenses
  (:require [pubs.comps.ui :as ui]
            [pubs.comps.option :as option]
            [pubs.comps.selector-button :as sb]
            )
  )

(defn- handle-ack [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:licenses/ack])
  )

(defn- acknowledge [s]
  [:div.details.last-child
   [:div.inner
    [:header "License acknowledgement"]
    [:div.ui.checkbox.inline
     [:input.important {:type :checkbox
                        :name :ack
                        :checked (or (get-in s [:data :ack]) false) 
                        :onChange #(handle-ack s %)
                        } ]
     [:label {:for :poc}
      "I have read the "
      [:a {:href (get-in s [:data :licenses :url])
           :target :_blank
           } "license terms"]
      " and agree to license my work under the attribution 3.0 unported license."
      ]
     ]
    ]
   ] 
  )

(defn license-item [s n detail]
  [:div.item {:key n}
   [:div.main
    [:header.subject n]   
    [:div.details.meta detail]  
    ]
   ]
  )

(defn- options [s k e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:panels/show k true])
  (re-frame.core/dispatch [:req/licenses])
  )

(defn render [s]
  [:div.field.err {:class (if (or (get-in s [:ui :errors :licenses])
                                  (get-in s [:ui :errors :ack])
                                  ) 
                            :with-error)}
   [:label {:for :title} "License:"]
   (merge
     [:div.collection.single-item
      [:div.item
       [:div.main
        [:header.subject
         (get-in s [:data :licenses :title])
         ]
        [:div.meta
         (get-in s [:data :licenses :info])
         ]
        ] 
       ]
      (if (get-in s [:data :licenses]) (acknowledge s))
      (sb/render s :licenses nil options)
      ]   
     )
   (ui/val-error s :licenses)
   (ui/val-error s :ack)
   ]
  )
 
