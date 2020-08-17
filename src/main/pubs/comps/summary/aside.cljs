(ns pubs.comps.summary.aside
  (:require [pubs.handlers.validate :as validate] 
            [pubs.comps.summary.instructions :as instructions]
            [pubs.comps.summary.submit-button :as submit-button]
            )
  )

(defn render [s]
  [:aside
   [:div.inner
    [:div.notification
     (instructions/render s)   
     [:fieldset.buttons-aside
      (if (validate/valid? s) (submit-button/render s))
      [:a.btn.secondary {:href (str
                                 "/pubs/#/pubs/" (get-in s [:data :pub-id])
                                 "/v/" (get-in s [:data :ver-id])
                                 "/edit")} "Edit draft"]
      [:a.btn.secondary {:href (str "/projects/"
                                    (get-in s [:data :prj-id])
                                    "/publications"
                                    )} "Save & Close"]
      ]
     ]
    ]
   ]
  )
 
