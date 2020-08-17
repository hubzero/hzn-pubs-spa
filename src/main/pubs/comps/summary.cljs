(ns pubs.comps.summary
  (:require [pubs.comps.summary.aside :as aside]
            [pubs.comps.summary.main :as main]
            [pubs.handlers.validate :as validate]
            )
  )

;(defn- _submit [s e]
;  ;; This will trigger the backend to grab a DOI - JBG
;  (swap! s assoc-in [:data :state] 1)
;  (data/submit-pub s) 
;  )
;

(defn render [s]
  [:div.page.page-summary.--add.--show {:class (if (get-in s [:ui :summary])
                                                 [:add :show]
                                                 )}
   (main/render s)
   (if (not (validate/submitted? s)) (aside/render s))
   ]
  )

