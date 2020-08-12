(ns pubs.handlers.pub-date)

(defn select [db [_ date]]
  (assoc-in db [:data :publication-date] (.format date "MM/DD/YYYY"))
  )

