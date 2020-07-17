(ns pubs.events-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [re-frame.core :as rf :refer [dispatch dispatch-sync]]
            [day8.re-frame.test :as rf-test]
            [pubs.events :as events]
            )
  )


(rf/reg-sub :content (fn [db _] (get-in db [:data :content])))
(rf/reg-sub :authors (fn [db _] (get-in db [:data :authors-list])))
(rf/reg-sub :user-id (fn [db _] (get-in db [:data :user-id]))) 
(rf/reg-sub :mt (fn [db _] (:master-types db)))
(rf/reg-sub :data (fn [db _] (:data db))) 
(rf/reg-sub :usage (fn [db _] (:usage db)))
(rf/reg-sub :owners (fn [db _] (:users db)))

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

(deftest authors 
  (with-redefs [pubs.hub/pub pubs.hub-test/pub
                pubs.hub/authors pubs.hub-test/authors]
    (rf-test/run-test-sync
      (let [authors (rf/subscribe [:authors])]
        (dispatch [:req/authors])
        (->> @authors nil? not is)
        (->> @authors count (= 4) is)
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

(deftest owners 
  (with-redefs [pubs.hub/owners pubs.hub-test/owners]
    (rf-test/run-test-sync
      (let [owners (rf/subscribe [:owners])]
        (dispatch [:req/owners])
        (->> @owners nil? not is)
        (->> @owners count (= 1) is)
        )
      )
    )
  )

(deftest rm-author
  (with-redefs [pubs.hub/authors pubs.hub-test/authors
                pubs.hub/rm-author pubs.hub-test/rm-author]
    (rf-test/run-test-sync
      (let [authors (rf/subscribe [:authors])]
        (dispatch [:req/authors])
        (-> @authors count (= 4) is)
        (dispatch [:authors/rm :456])
        (-> @authors count (= 3) is)
        )
      )
    )
  )

(deftest add-author
  (with-redefs [pubs.hub/authors pubs.hub-test/authors
                pubs.hub/add-author pubs.hub-test/add-author]
    (rf-test/run-test-sync
      (let [a {:role 2, :projectid 1, :userid 0, :created_by_user 1001, :lastname nil, :added "2020-01-29T10:32:28Z", :name nil, :invited_email nil, :username nil, :groupname nil, :params nil, :fullname "Femke Blokje", :invited_name "Femke Blokje", :invited_code "JPNCMSQLHN", :organization nil, :lastvisit nil, :num_visits 0, :firstname nil, :native 0, :groupid 0, :status 0, :id 41, :picture nil, :groupdesc nil, :prev_visit nil}
            authors (rf/subscribe [:authors])]
        (dispatch [:req/authors])
        (-> @authors count (= 4) is)
        (dispatch [:authors/add a])
        (-> @authors count (= 5) is)
        )
      )
    )
  )


