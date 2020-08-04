(ns pubs.comps.form
  (:require [pubs.comps.additional-details :as ad]
            [pubs.comps.essentials :as essentials])
  )

(defn render [s]
  [:main
   [:form
    (essentials/render s) 
    (ad/render s)
    ;(publish-settings s)
    ;(section-buttons s)
    ]
   ]
  )

