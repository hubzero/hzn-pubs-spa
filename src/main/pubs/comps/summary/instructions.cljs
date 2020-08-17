(ns pubs.comps.summary.instructions
  (:require [pubs.handlers.validate :as validate])
  )

(defn- valid? [s]
  ;; Terms is not really persistent, so not checking it? - JBG
  (-> (validate/pub s) (dissoc :terms) count (= 0))
  )

(defn render [s]
  (if (valid? s)
    [:div
     [:header "Your publication is ready for submission!"] 
     [:p "Please review your publication and make sure everything looks good."]
     ]
    [:div
     [:header "Not quite yet."] 
     [:p "Please complete Essential fields in order to submit."]
     ]
    )
  )

 
