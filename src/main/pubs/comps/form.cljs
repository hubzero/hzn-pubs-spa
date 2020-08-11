(ns pubs.comps.form
  (:require [pubs.comps.additional-details :as ad]
            [pubs.comps.essentials :as es]
            [pubs.comps.pub-settings :as ps]
            )
  )

(defn render [s]
  [:main
   [:form
    (es/render s) 
    (ad/render s)
    (ps/render s)
    ;(section-buttons s)
    ]
   ]
  )

