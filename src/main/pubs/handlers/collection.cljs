(ns pubs.handlers.collection)

(defn order [db [_ k l]]
  ;; Update all the author indexes/sort order - JBG
  (doall
    (case k
      :authors-list
      (map-indexed (fn [i a]
                     (re-frame.core/dispatch [:authors/modify (assoc a :index i)])
                     ) (vals l))
      (prn "skip ordering" k)
      )
    ) 
  (assoc db [:data k] l)
  )

