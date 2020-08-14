(ns pubs.comps.summary.publish-settings
  (:require [pubs.comps.summary.section :as section])
  )

(defn render [s]
  (section/render s [[[:data :publication-date] "Embargo date" :text false]
                     [[:data :comments] "Comments to the administrator" :text false]
                     ])
  )

