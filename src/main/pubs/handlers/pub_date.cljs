(ns pubs.handlers.pub-date)

(defn select [db [_ date]]
  (assoc-in db [:data :publication-date] (.format date "MM/DD/YYYY"))
  )

(defn change [db [_ date]]
  (if (= (count date) 0)
    (update db :data dissoc :publication-date)
    ) 
  )

