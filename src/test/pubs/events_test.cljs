(ns pubs.events-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [re-frame.core :as rf :refer [dispatch dispatch-sync]]
            [day8.re-frame.test :as rf-test]
            [pubs.events :as events]
            )
  )

(deftest me
  (with-redefs [pubs.hub/me pubs.hub-test/me]
    (rf/reg-sub :user-id (fn [db _] (get-in db [:data :user-id]))) 
    (rf-test/run-test-sync
      (let [user-id (rf/subscribe [:user-id])]
        (dispatch [:req/me])
        (-> @user-id (= 1001) is)
        )
     )
    )
  )

(deftest master-types
  (with-redefs [pubs.hub/pub pubs.hub-test/pub
                pubs.hub/master-types pubs.hub-test/master-types]

    (rf/reg-sub :mt (fn [db _] (:master-types db)))
    (rf/reg-sub :data (fn [db _] (:data db)))

    (rf-test/run-test-sync
      (let [mt (rf/subscribe [:mt])
            data (rf/subscribe [:data])
            ]
        (dispatch [:req/pub])

        (->> @mt nil? not is)
        (->> @data (:master-type) (:master-type) nil? not is)
        ;; authors
        ;;tags
        ;;citations

        )
      )
    )
  )

