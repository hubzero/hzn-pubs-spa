(ns pubs.comps.form
  (:require [pubs.comps.essentials :as essentials])
  )

(defn render [s]
  [:main
   [:form
    (essentials/render s) 
    ;(additional-details s)
    ;(publish-settings s)
    ;(section-buttons s)
    ]
   ]
  )
 
