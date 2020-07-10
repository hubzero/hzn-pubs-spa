(ns pubs.containers.app
  (:require [re-frame.core :as rf]
            [pubs.subs :as subs]
            [pubs.comps.app :as app]
            )
  )

(defn render []
  (let [data @(rf/subscribe [::subs/data])]
    (if (get-in data [:data :pub-id])
      (app/render data)
      )
    )
  )

