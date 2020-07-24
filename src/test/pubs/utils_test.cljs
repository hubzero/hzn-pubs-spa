(ns pubs.utils-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [pubs.utils :as utils]
            )
  )

(deftest ->int
  (->> (utils/->int :1000)
       integer?
       is
       )
  )

(deftest ->keyword 
  (->> (utils/->keyword 1000)
       (= :1000)
       is
       )
  )
