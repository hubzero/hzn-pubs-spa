(ns pubs.comps.usage)

(defn render [s]
  [:div {:class [:ui :progress-bar]}
   [:div {:class :status}
    [:strong (str (get-in s [:usage :size])
                  (get-in s [:usage :units])
                  " ("
                  (get-in s [:usage :percent])
                  "%)"
                  )]
    " of your "
    [:strong (str (get-in s [:usage :max])
                  (get-in s [:usage :units])
                  )]
    ]
   [:div {:class :progress}
    [:div {:class :bar
           :style {:right (str (- 100 (get-in s [:usage :percent])) "%")}}
     [:span (str (get-in s [:usage :percent]) "%")]
     ]
    ]
   ]
  )

