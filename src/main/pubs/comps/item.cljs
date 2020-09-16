(ns pubs.comps.item
  (:require [pubs.comps.author :as author]
            [pubs.comps.citation :as citation]
            [pubs.comps.database :as database]
            [pubs.comps.file :as file]
            [pubs.comps.image :as image]
            [pubs.comps.series :as series]
            )
  )

(defn render [s k v]
  ((k {
       :content #(file/render s k (second v) (first v))
       :support-docs #(file/render s k (second v) (first v))
       :authors-list #(author/render s k (second v) (first v))
       :images #(image/render s k (second v) (first v))
       :citations #(citation/render s k (second v))
       :databases #(database/render s k (second v))
       :series #(series/render s k (second v))
       }))
  )

