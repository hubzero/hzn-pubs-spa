(ns pubs.res-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [pubs.res :as res]
            [pubs.hub-test :refer [resps]]
            )
  )

(deftest me
  (-> (res/me {} [:res/me (:me resps)])
      (get-in [:data :user-id])
      (= 1001)
      is
      ) 
  )

(deftest master-types
  (let [db (res/master-types {} [:res/master-types (:master-types resps)]) ]
    (-> db (:master-types) count (= 3) is)  
    )
  )

(deftest pub-master-type
  (let [db (res/master-types {} [:res/master-types (:master-types resps)])
        pub (:pub resps)
        ]
    (->> (res/pub-master-type db pub)
         (:master-type)
         (:master-type)
         (= "File(s)")
         is
        )
    )
  )

(deftest pub
  (let [db (res/pub {} [:res/pub (:pub resps)])]
    (-> db (get-in [:data :prj-id]) (= 1) is)
    (-> db (get-in [:data :pub-id]) (= 209) is)
    (-> db (get-in [:data :ver-id]) (= 196) is)
    )
  )

