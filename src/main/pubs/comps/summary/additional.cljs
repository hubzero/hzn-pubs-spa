(ns pubs.comps.summary.additional
  (:require [pubs.comps.summary.section :as section])
  )

(defn render [s]
  (section/render s [[[:data :images] "Image Gallery" :images false]
               [[:data :url] "External website URL" :text false]
               [[:data :support-docs] "Supporting documents" :files false]
               [[:data :citations] "Citations" :citations false]
               [[:data :release-notes] "Version release notes" :text false]
               ])
  )
 
