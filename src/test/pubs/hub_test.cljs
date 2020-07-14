(ns pubs.hub-test
  (:require [re-frame.core :as rf :refer [dispatch dispatch-sync]]
            )
  )

(def resps {:me {:id 1001, :username "jbg"} 
            :master-types [{:description "uploaded material", :supporting 1, :params "peer_review=1", :curation nil, :type "File(s)", :curatorgroup nil, :ordering 1, :alias "files", :id 1, :contributable 1} {:description "external content", :supporting 0, :params "", :curation nil, :type "Link", :curatorgroup nil, :ordering 3, :alias "links", :id 2, :contributable 0} {:description "from project notes", :supporting 0, :params "", :curation nil, :type "Wiki", :curatorgroup nil, :ordering 5, :alias "notes", :id 3, :contributable 0} {:description "simulation tool", :supporting 0, :params "", :curation nil, :type "Application", :curatorgroup nil, :ordering 4, :alias "apps", :id 4, :contributable 0} {:description "publication collection", :supporting 0, :params "", :curation nil, :type "Series", :curatorgroup nil, :ordering 6, :alias "series", :id 5, :contributable 0} {:description "image/photo gallery", :supporting 0, :params "", :curation nil, :type "Gallery", :curatorgroup nil, :ordering 7, :alias "gallery", :id 6, :contributable 0} {:description "project database", :supporting 0, :params "", :curation nil, :type "Databases", :curatorgroup nil, :ordering 2, :alias "databases", :id 7, :contributable 0}]
            :pub {:pub-id 209, :ver-id 196, :prj-id 1, :comments "Blha blha blha", :abstract "blah foo bar", :user-id 1001, :state 1, :doi "10.21978/J37M-WH80", :title "Hahaha --- blah blah balh", :master-type 1, :publication-date "04/26/2020", :ack true, :url nil, :license_type nil, :release-notes "hahahahaha"}
            :files {:content {:1264 {:path "broodje/files/c6e1e2e4-c0a0-4e65-b3c2-a66268f40c27.jpg", :name "c6e1e2e4-c0a0-4e65-b3c2-a66268f40c27.jpg", :id 1264, :index 0}}}
            :rm-file ""
            :ls-files [["broodje/files" ["Foo"] []] ["broodje/files/Foo" ["Bar"] []] ["broodje/files/Foo/Bar" ["Baz"] []] ["broodje/files/Foo/Bar/Baz" [] ["Screenshot_2020-07-02 PURR - PURR.png" "Screen Shot 2020-06-08 at 1.45.09 PM.png"]]]
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

(defn rm-file [db k id]
  (dispatch [:res/rm-file [(:rm-file resps) k id]])
  db
  )

(defn ls-files [db]
  (dispatch [:res/ls-files (:ls-files resps)])
  db
  )

