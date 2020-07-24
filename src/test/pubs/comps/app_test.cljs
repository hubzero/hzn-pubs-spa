(ns pubs.comps.app-test
  (:require [cljs.test :refer (deftest is)]
            [pubs.events :as events]
            [re-frame.core :as rf]
            [pubs.enzyme :refer [shallow]]
            [pubs.comps.app :refer [render]]))

(deftest app 
  (-> (render {})
      shallow
      .text 
      (.indexOf "Essentials")
      (= -1)
      not
      is
      )
  )

