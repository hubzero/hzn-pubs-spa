(ns pubs.comps.summary.essentials
  (:require [pubs.comps.summary.section :as section])
  )

(defn render [s]
  (section/render s [[[:data :doi] "DOI" :text true]
                     [[:data :title] "Title" :text true]
                     [[:data :abstract] "Abstract" :text false]
                     [[:data :content] "Content" :files false]
                     [[:data :authors-list] "Authors" :authors-list false]
                     [[:data :tags] "Tags" :tags false]
                     [[:data :licenses] "License" :license false]
                     [[:terms] "Agreements" :text false]
                     ]) 
  )

