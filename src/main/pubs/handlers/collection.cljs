(ns pubs.handlers.collection
  (:require [pubs.hub :as hub]
            [pubs.utils :as utils]
            )
  )

(defn order [db [_ k l]]
  ;; Update all the author indexes/sort order - JBG
  (doall
    (case k
      :authors-list (->>
                      (map-indexed (fn [i a]
                                     (let [a' (assoc a :index i)]
                                       (hub/update-author db a')  
                                       [(utils/->keyword (:id a')) a']
                                       )
                                     ) (vals l))
                      (into {})
                      (assoc-in db [:data k])
                      )
      db
      )
    )
  )

