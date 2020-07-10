(ns pubs.handlers.text)

(defn change [db [_ k v]]
  (assoc-in db [:data k] v)
  )

