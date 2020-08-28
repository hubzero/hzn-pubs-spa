(ns pubs.handlers.collection
  (:require [pubs.hub :as hub]
            [pubs.utils :as utils]
            )
  )

(defn- index [db k l f]
  (->>
    (map-indexed (fn [i a]
                   (let [a' (assoc a :index i)]
                     (f db a' k)
                     [(utils/->keyword (:id a')) a']
                     )
                   ) (vals l))
    (into {})
    (assoc-in db [:data k])
    )
  )

(defn order [db [_ k l]]
  ;; Update all the indexes/sort order - JBG
  (doall
    (case k
      :authors-list (index db k l hub/update-author)
      :content (index db k l hub/update-file)
      db
      )
    )
  )

