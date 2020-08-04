(ns pubs.handlers.options)

(defn show [db [_ k id]]
  (assoc-in db [:ui :options k id] true)
  )

(defn close [db _]
  (assoc-in db [:ui :options] nil)
  )

(defn authors [db _]
  (assoc-in db [:ui :options :authors] true)
  )

(defn citations [db _]
  (assoc-in db [:ui :options :citations] true)
  )

