(ns pubs.comps.app-test
  (:require [cljs.test :refer (deftest is)]
            [pubs.events :as events]
            [re-frame.core :as rf]
            [pubs.enzyme :refer [shallow]]
            [pubs.comps.app :refer [render]]))

(deftest app 
  ;; Initialize the state
  (rf/dispatch-sync [::events/initialize-db])
  ;; Render the comp
  (-> (render)
      shallow
      .text 
      (.indexOf "pubs")
      (= -1)
      not
      is
      )
  )

