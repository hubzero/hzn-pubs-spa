(ns pubs.comps.summary.instructions
  (:require [pubs.handlers.validate :as validate])
  )

(defn render [s]
  (if (validate/valid? s)
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

 
