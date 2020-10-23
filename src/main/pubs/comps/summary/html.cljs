(ns pubs.comps.summary.html)

(defn render [state k bold?]
  [:p
   {:dangerouslySetInnerHTML {:__html (get-in state k)}}])
