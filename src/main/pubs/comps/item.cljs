(ns pubs.comps.item
  (:require [pubs.comps.author :as author]
            [pubs.comps.file :as file]
            )
  )

(defn render [s k v]
  ((k {
       :content #(file/render s k (second v) (first v))
       :support-docs #(file/render s k (second v) (first v))
       :authors-list #(author/render s k (second v) (first v))
       ;:images #(image s k (second v) (first v))
       ;:citations #(citation s k (second v))
       })) 
  )

