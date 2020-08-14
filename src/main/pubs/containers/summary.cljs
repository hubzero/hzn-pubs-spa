(ns pubs.containers.summary
  (:require [re-frame.core :as rf]
            [pubs.subs :as subs]
            [pubs.comps.summary :as summary]
            )
  )

(defn render []
  (let [data @(rf/subscribe [::subs/data])]
    (if (get-in data [:data :pub-id])
      (summary/render data)
      )
    )
  )

