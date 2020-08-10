(ns pubs.comps.item
  (:require [pubs.comps.author :as author]
            [pubs.comps.citation :as citation]
            [pubs.comps.file :as file]
            [pubs.comps.image :as image]
            )
  )

(defn render [s k v]
  ((k {
       :content #(file/render s k (second v) (first v))
       :support-docs #(file/render s k (second v) (first v))
       :authors-list #(author/render s k (second v) (first v))
       :images #(image/render s k (second v) (first v))
       :citations #(citation/render s k (second v))
       }))
  )

