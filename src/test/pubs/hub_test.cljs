(ns pubs.hub-test
  (:require [re-frame.core :as rf :refer [dispatch dispatch-sync]]
            )
  )

(def resps {:me {:id 1001, :username "jbg"} 
            :master-types [{:description "uploaded material", :supporting 1, :params "peer_review=1", :curation nil, :type "File(s)", :curatorgroup nil, :ordering 1, :alias "files", :id 1, :contributable 1} {:description "external content", :supporting 0, :params "", :curation nil, :type "Link", :curatorgroup nil, :ordering 3, :alias "links", :id 2, :contributable 0} {:description "from project notes", :supporting 0, :params "", :curation nil, :type "Wiki", :curatorgroup nil, :ordering 5, :alias "notes", :id 3, :contributable 0} {:description "simulation tool", :supporting 0, :params "", :curation nil, :type "Application", :curatorgroup nil, :ordering 4, :alias "apps", :id 4, :contributable 0} {:description "publication collection", :supporting 0, :params "", :curation nil, :type "Series", :curatorgroup nil, :ordering 6, :alias "series", :id 5, :contributable 0} {:description "image/photo gallery", :supporting 0, :params "", :curation nil, :type "Gallery", :curatorgroup nil, :ordering 7, :alias "gallery", :id 6, :contributable 0} {:description "project database", :supporting 0, :params "", :curation nil, :type "Databases", :curatorgroup nil, :ordering 2, :alias "databases", :id 7, :contributable 0}]
            :pub {:pub-id 209, :ver-id 196, :prj-id 1, :comments "Blha blha blha", :abstract "blah foo bar", :user-id 1001, :state 1, :doi "10.21978/J37M-WH80", :title "Hahaha --- blah blah balh", :master-type 1, :publication-date "04/26/2020", :ack true, :url nil, :license_type nil, :release-notes "hahahahaha"}
            :files {:content {:1264 {:path "broodje/files/c6e1e2e4-c0a0-4e65-b3c2-a66268f40c27.jpg", :name "c6e1e2e4-c0a0-4e65-b3c2-a66268f40c27.jpg", :id 1264, :index 0}}}
            :add-file {:generated_key 1401}
            :rm-file ""
            :ls-files [["broodje/files" ["Foo"] []] ["broodje/files/Foo" ["Bar"] []] ["broodje/files/Foo/Bar" ["Baz"] []] ["broodje/files/Foo/Bar/Baz" [] ["Screenshot_2020-07-02 PURR - PURR.png" "Screen Shot 2020-06-08 at 1.45.09 PM.png"]]]
            :usage {:size "0.0000", :units "GB", :percent "0.00", :max "1.00"}
            :authors {:422 {:email "fb@example.com", :project_owner_id 41, :userid 0, :index 1, :lastname "Blok", :fullname "Femke Blokje", :organization "Homegirl", :firstname "Femke", :id 422, :poc 0, :credit ""}, :431 {:email "ps@example.com", :project_owner_id 42, :userid 0, :index 3, :lastname "Smart", :fullname "Petra Smart", :organization "foo", :firstname "Petra", :id 431, :poc 0, :credit ""}, :452 {:email "jbg@example.com", :project_owner_id 1, :userid 0, :index 0, :lastname "G", :fullname "J B G", :organization "Bob Taco Stand Baz", :firstname "J B", :id 452, :poc 0, :credit ""}, :456 {:email "bos@example.com", :project_owner_id 107, :userid 0, :index 2, :lastname nil, :fullname "Annet Bos", :organization nil, :firstname nil, :id 456, :poc 0, :credit ""}}
            :owners [{:role 1, :projectid 121, :userid 1001, :created_by_user 1001, :lastname "G", :added "2020-04-22T14:16:38Z", :name "J B G", :invited_email "", :username "jbg", :groupname nil, :params nil, :fullname "J B G", :invited_name nil, :invited_code nil, :organization "", :lastvisit nil, :num_visits 0, :firstname "J", :native 1, :groupid 0, :status 1, :id 133, :picture "", :groupdesc nil, :prev_visit nil}]
            }
  )

(defn me [db]
  (dispatch [:res/me (:me resps)])
  db
  )

(defn master-types [db]
  (dispatch [:res/master-types (:master-types resps)])
  db
  )

(defn pub [db]
  (dispatch [:res/pub (:pub resps)])
  db
  )

(defn files [db]
  (dispatch [:res/files (:files resps)])
  db
  )

(defn add-file [db file]
  (dispatch [:res/add-file (:add-file resps) file])
  db
  )

(defn rm-file [db k id]
  (dispatch [:res/rm-file (:rm-file resps) k id])
  db
  )

(defn ls-files [db]
  (dispatch [:res/ls-files (:ls-files resps)])
  db
  )

(defn usage [db files]
  (dispatch [:res/usage (:usage resps)])
  db
  )

(defn authors [db]
  (dispatch [:res/authors (:authors resps)])
  db
  )

(defn owners [db]
  (dispatch [:res/owners (:owners resps)])
  db
  )

