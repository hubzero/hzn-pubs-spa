(ns pubs.handlers.collection
  (:require [pubs.hub :as hub]
            [pubs.utils :as utils]
            )
  )

(defn- index [db k l f]
  "The internal database is storing a map structure with an :index field that needs to be updated.
   The incoming list `l` is an in-order vector, so we need to generate the index from the position
   of this incoming list, and then trigger persistence on the back end with the
   `f` call."
  (let [v (map-indexed (fn [idx [vk vmap]]
                         (let [indexed (assoc vmap :index idx)]
                           (f db indexed k)
                           [(utils/->keyword (:id vmap)) indexed]))
                       l)]
    (assoc-in db [:data k] (into {} v))))

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

