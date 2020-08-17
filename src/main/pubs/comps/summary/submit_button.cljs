(ns pubs.comps.summary.submit-button)

(defn- submit [s e]
  (re-frame.core/dispatch [:req/submit])
  )

(defn render [s]
  [:a.btn {:href (str
                   "/pubs/#/pubs/" (get-in s [:data :pub-id])
                   "/v/" (get-in s [:data :ver-id])
                   )
           :on-click #(submit s %)
           } "Submit publication"]
  )
 
