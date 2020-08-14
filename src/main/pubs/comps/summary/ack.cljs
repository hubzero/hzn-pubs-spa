(ns pubs.comps.summary.ack)

(defn render [s]
  [:div {:class [:details :last-child]}
   [:div {:class :inner}
    [:header "License acknowledgement"]
    [:p {:class :font-small}
     (str "You have read the license terms and agreed to license you work under " (get-in s [:data :licenses :name]) ".")
     ]
    ]
   ]
  )
 
