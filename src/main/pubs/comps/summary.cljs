(ns pubs.comps.summary
  (:require [pubs.comps.summary.main :as main] 
    ;[hzn-pubs-spa.data :as data] 
    ;[hzn-pubs-spa.comps.ui :as ui] 
    )
  )

;(defn- _submit [s e]
;  ;; This will trigger the backend to grab a DOI - JBG
;  (swap! s assoc-in [:data :state] 1)
;  (data/submit-pub s) 
;  )
;
;(defn submit-button [s]
;  [:a.btn {:href (str
;                   "/pubs/#/pubs/" (get-in @s [:data :pub-id])
;                   "/v/" (get-in @s [:data :ver-id])
;                   )
;           :on-click #(_submit s %)
;           } "Submit publication"]
;  )
;
;(defn- _submit-instructions [s]
;  (if (utils/valid? s)
;    [:div
;     [:header "Your publication is ready for submission!"] 
;     [:p "Please review your publication and make sure everything looks good."]
;     ]
;    [:div
;     [:header "Not quite yet."] 
;     [:p "Please complete Essential fields in order to submit."]
;     ]
;    )
;  )
;
;(defn aside [s]
;  [:aside
;   [:div.inner
;    [:div.notification
;     (_submit-instructions s)   
;     [:fieldset.buttons-aside
;      (if (utils/valid? s) (submit-button s))
;      [:a.btn.secondary {:href (str
;                                 "/pubs/#/pubs/" (get-in @s [:data :pub-id])
;                                 "/v/" (get-in @s [:data :ver-id])
;                                 "/edit")} "Edit draft"]
;      [:a.btn.secondary {:href (str "/projects/"
;                                    (get-in @s [:data :prj-id])
;                                    "/publications"
;                                    )} "Save & Close"]
;      ]
;     ]
;    ]
;   ]
;  )

(defn render [s]
  [:div.page.page-summary.--add.--show {:class (if (get-in s [:ui :summary])
                                                 [:add :show]
                                                 )}
   (main/render s)
   ;(if (not (= 1 (get-in @s [:data :state]))) (aside s))
   ]
  )

