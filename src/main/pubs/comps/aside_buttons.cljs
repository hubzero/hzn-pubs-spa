(ns pubs.comps.aside-buttons
  (:require [pubs.handlers.validate :as validate])
  )

(defn- submit [s e]
  (re-frame.core/dispatch [:req/save-pub])
  (let [errors (validate/pub s)]
    (when (> (count errors) 0)
      (.preventDefault e) 
      (.stopPropagation e)
      (re-frame.core/dispatch [:panels/errors errors])
      )
    )
  )

(defn render [s]
  [:aside
   [:div.inner
    [:fieldset.buttons-aside
     ;;[:a.btn {:href "/pubs/#/summary"} "Proceed with the draft"]
     ;;[:a.btn {:href "#" :on-click #(_submit-draft s %)} "Proceed with the draft"]
     [:a.btn {:href (str "/pubs/#/pubs/"
                         (get-in s [:data :pub-id])
                         "/v/"
                         (get-in s [:data :ver-id])
                         )
              :on-click #(submit s %)} "Proceed with the draft"]
     [:a.btn.secondary {:href (str "/projects/"
                                   (get-in s [:data :prj-id])
                                   "/publications"
                                   )} "Save & Close"]
     ;; https://localhost/projects/broodje/publications/274/continue
     [:a.btn.secondary {:href (str "/projects/"
                                   (get-in s [:data :prj-id])
                                   "/publications/"
                                   (get-in s [:data :pub-id])
                                   "/continue"
                                   )} "Switch to classic"]
     ]
    ]
   ]
  )

