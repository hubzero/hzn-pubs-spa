(ns pubs.comps.section-buttons
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
  [:fieldset.fieldset-section.buttons
   [:div.field.buttons
    [:a.btn {:href (str "/pubs/#/pubs/"
                        (get-in s [:data :pub-id])
                        "/v/"
                        (get-in s [:data :ver-id])
                        )
             :on-click #(submit s %)} "Proceed with the draft"]
    ]
   ]
  )

