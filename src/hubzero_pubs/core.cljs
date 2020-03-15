(ns hubzero-pubs.core
  (:require [reagent.core :as r]
            [hubzero-pubs.routes :as routes]
            [hubzero-pubs.utils :as utils]
            [hubzero-pubs.data :as data]
            [hubzero-pubs.comps.app :as app]
            )
  )

(defonce s (r/atom {:terms "I and all publication authors have read and agree to PURR terms of deposit." 
                    :months ["January" "February" "March" "April" "May" "June" "July" "August" "September" "October" "November" "December"]
                    :l [{:id 1 :name "foo"}
                        {:id 2 :name "bar"}
                        ]
                    }
                   ))

(defn on-js-reload [])
(routes/app-routes s)

(defn run []
  (data/get-user s)
  (r/render [#(app/app s)] (js/document.getElementById "app"))
  )

(defn main! []
  (-> js/document (.addEventListener "DOMContentLoaded" run))
  )



