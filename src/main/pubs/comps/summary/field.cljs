(ns pubs.comps.summary.field)

(defn render [s k bold?]
  (if bold?
    [:h2 (get-in s k)]
    [:p (get-in s k)]
    ) 
  )
 
