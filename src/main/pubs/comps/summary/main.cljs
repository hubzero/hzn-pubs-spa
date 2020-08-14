(ns pubs.comps.summary.main
  (:require [pubs.comps.summary.essentials :as essentials]
;            [pubs.comps.summary.additional :as additional]
;            [pubs.comps.summary.publish-settings :as publish-settings]
            )
  )

(defn render [s]
  [:main
   (essentials/render s)   
   ;(additional/render s)
   ;(publish-settings/render s)
   ]
  )
 
