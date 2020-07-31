(ns pubs.handlers.tags
  (:require [pubs.hub :as hub])
  )

(defn creator [db _]
  (assoc-in db [:ui :tag] true)
  )

(defn rm [db [_ id]]
  (hub/rm-tag db id)
  )

(defn add [db [_ tag-str]]
  (hub/add-tag db tag-str)
  )

(defn search [db [_ tag-str]]
  (as-> db $
    (if (and (not (empty? tag-str)) (> (count tag-str) 2))
      (assoc $ :tag-query tag-str)
      db
      )
    (assoc-in $ [:ui :tag-str] tag-str)
    (hub/search-tags $ tag-str)
    )
  )

