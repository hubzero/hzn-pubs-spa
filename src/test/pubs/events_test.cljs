(ns pubs.events-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [re-frame.core :as rf :refer [dispatch dispatch-sync]]
            [day8.re-frame.test :as rf-test]
            [pubs.events :as events]
            )
  )


(rf/reg-sub :content (fn [db _] (get-in db [:data :content])))
(rf/reg-sub :user-id (fn [db _] (get-in db [:data :user-id]))) 
(rf/reg-sub :mt (fn [db _] (:master-types db)))
(rf/reg-sub :data (fn [db _] (:data db))) 
(rf/reg-sub :usage (fn [db _] (:usage db)))

(deftest me
  (with-redefs [pubs.hub/me pubs.hub-test/me]
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
    (rf-test/run-test-sync
      (let [mt (rf/subscribe [:mt])
            data (rf/subscribe [:data])
            ]
        (dispatch [:req/pub])
        (->> @mt nil? not is)
        (->> @data (:master-type) (:master-type) nil? not is)
        )
      )
    )
  )

(deftest add-file
  (with-redefs [pubs.hub/files pubs.hub-test/files
                pubs.hub/add-file pubs.hub-test/add-file]
    (rf-test/run-test-sync
      (let [content (rf/subscribe [:content])]
        (dispatch [:req/files])
        (dispatch [:req/add-file {:type :content
                                  :index 2
                                  :path "prjfoobar/files/foo"
                                  :name "foo"}])
        (-> @content count (= 2) is)
        (->> @content keys (every? keyword?) is)
        )
      )
    )
  )

(deftest rm-file
  (with-redefs [pubs.hub/files pubs.hub-test/files
                pubs.hub/rm-file pubs.hub-test/rm-file]
    (rf-test/run-test-sync
      (let [content (rf/subscribe [:content])]
        (dispatch [:req/files])
        (dispatch [:req/rm-file :content :1264])
        (-> @content count (= 0) is)
        )
      )
    )
  )

(deftest usage 
  (with-redefs [pubs.hub/pub pubs.hub-test/pub
                pubs.hub/usage pubs.hub-test/usage]
    (rf-test/run-test-sync
      (let [usage (rf/subscribe [:usage])]
        (dispatch [:req/usage])
        (->> @usage nil? not is)
        )
      )
    )
  )

