(ns pubs.comps.summary.essentials
  (:require [pubs.comps.summary.section :as section])
  )

(defn- content-type [s]
  (case (get-in s [:data :master-type :master-type])
    "Databases" [[:data :databases] "Databases" :databases false]
    "Series" [[:data :series] "Series" :series false]
    [[:data :content] "Content" :files false]
    )
  )

(defn render [s]
  (section/render s [[[:data :doi] "DOI" :text true]
                     [[:data :title] "Title" :text true]
                     [[:data :abstract] "Abstract" :text false]
                     (content-type s)
                     [[:data :authors-list] "Authors" :authors-list false]
                     [[:data :tags] "Tags" :tags false]
                     [[:data :licenses] "License" :license false]
                     [[:terms] "Agreements" :text false]
                     ]) 
  )

