(ns pubs.comps.master-types
  (:require [pubs.comps.dropdown :as dropdown])  
  )

(defn render [s]
  (dropdown/render s :master-type {:name :master-type
                                   :label "Master Type"
                                   :options (map #(:type %) (:master-types s))
                                   })
  )

