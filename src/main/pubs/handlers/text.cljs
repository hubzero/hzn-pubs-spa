(ns pubs.handlers.text
  (:require [pubs.hub :as hub])
  )

(defn change [db [_ k v]]
  (assoc-in db [:data k] v)
  )

