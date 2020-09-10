(ns pubs.res-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [pubs.handlers.res :as res]
            [pubs.hub-test :refer [resps]]
            [pubs.utils :as utils]
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

(deftest files 
  (let [db (res/pub {} [:res/files (:files resps)])]
    (-> db (get-in [:data :content]) nil? not is)
    (-> db (get-in [:data :content :1264]) nil? not is)
    (-> db (get-in [:data :content :1264 :name]) nil? not is)
    (-> db (get-in [:data :content :1264 :path]) nil? not is)
    )
  )

(deftest ls-files 
  (let [db (res/ls-files {} [:res/ls-files (:ls-files resps)])]
    (-> db (:files) nil? not is)
    (-> db (:files) vector? is)
    (-> db (get-in [:ui :current-folder]) vector? is)
    (-> db (get-in [:ui :current-folder]) first vector? is)
    (-> db (get-in [:ui :current-folder]) first first (= "Project files"))
    (-> db (get-in [:ui :current-folder]) first second (= "broodje/files"))
    )
  )

(deftest usage 
  (let [db (res/usage {} [:res/usage (:usage resps)])]
    (-> db (:usage) nil? not is)
    )
  )

(deftest authors 
  (let [db (res/authors {} [:res/authors (:authors resps)])]
    (-> db (get-in [:data :authors-list]) nil? not is)
    )
  )

(deftest owners 
  (let [db (res/owners {} [:res/owners (:owners resps)])]
    (-> db (:users) nil? not is)
    )
  )

(deftest update-author 
  (let [db (-> (res/authors {} [:res/authors (:authors resps)])
               (res/update-author [:res/update-author (:update-author resps)]) )]
    (-> db (get-in [:data :authors-list]) nil? not is)
    (-> db (get-in [:data :authors-list]) count (= 4) is)
    )
  )

;; TODO: Fix me - JBG (Am I ashamed, yes)
;(deftest add-author 
;  (let [db (-> (res/authors {} [:res/authors (:authors resps)])
;               (res/add-author [:res/add-author (:add-author resps)]) )]
;    (-> db (get-in [:data :authors-list]) nil? not is)
;    (-> db (get-in [:data :authors-list]) count (= 5) is)
;    )
;  )
;
;(deftest rm-author 
;  (let [db (-> (res/authors {} [:res/authors (:authors resps)])
;               (res/rm-author [:res/rm-author (:rm-author resps) 456]) )]
;    (-> db (get-in [:data :authors-list]) nil? not is)
;    (-> db (get-in [:data :authors-list]) count (= 3) is)
;    )
;  )
 
(deftest search-users 
  (let [db (res/search-users {} [:res/search-users (:search-users resps)]) ]
    (-> db :user-results count (= 1) is)  
    )
  )

(deftest prj
  (let [db (res/prj {} [:res/prj (:prj resps)])]
    (-> db (get-in [:data :prj]) nil? not is)
    )
  )

(deftest new-author
  (let [auth (:new-author resps)
        db (res/new-author {} [:res/new-author auth])]
    (-> db (get-in [:data :authors-list]) nil? not is)
    (-> db (get-in [:data :authors-list]) count (= 1) is)
    (-> db (get-in [:data :authors-list (utils/->keyword (:id auth))]) nil? not is)
    )
  )

(deftest tags
  (let [db (res/tags {} [:res/tags (:tags resps)])]
    (-> db (get-in [:data :tags]) nil? not is)
    (-> db (get-in [:data :tags]) map? is)
    (-> db (get-in [:data :tags]) count (= 1) is)
    (-> db (get-in [:data :tags]) first second :raw_tag (= "core-foo"))
    )
  )

(deftest add-tag 
  (let [db (res/add-tag {:tag-query "foo"
                         :ui {:tag true}
                         } [:res/add-tag (:add-tag resps)])]
    (-> db (get-in [:ui :tag-str]) count (= 0) is)
    (-> db (get-in [:tag-query]) nil? is)
    (-> db (get-in [:ui :tag]) not is)
    )
  )

(deftest search-tags 
  (let [db (res/search-tags {} [:res/search-tags (:search-tags resps)])]
    (-> db :tag-results nil? not is)
    (-> db :tag-results count (= 2) is)
    (-> db :tag-results vector? is)
    )
  )

(deftest licenses 
  (let [db (res/licenses {} [:res/licenses (:licenses resps)])]
    (-> db :licenses nil? not is)
    (-> db :licenses count (= 2) is)
    (-> db :licenses vector? is)
    )
  )

(deftest license 
  (let [db (res/license {} [:res/license (:license resps)])]
    (-> db (get-in [:data :licenses]) nil? not is)
    (-> db (get-in [:data :licenses]) map? is)
    (-> db (get-in [:data :licenses :id]) (= 2) is)
    )
  )

(deftest save-pub 
  (let [db (res/save-pub {} [:res/save-pub (:save-pub resps)])]
    (-> db (get-in [:data :pub-id]) nil? not is)
    (-> db (get-in [:data :ver-id]) nil? not is)
    )
  )

(deftest citations 
  (let [db (res/citations {} [:res/citations (:citations resps)])]
    (-> db (get-in [:data :citations]) nil? not is)
    (-> db (get-in [:data :citations]) map? is)
    (-> db (get-in [:data :citations]) count (= 1) is)
    (-> db (get-in [:data :citations]) first first keyword? is)
    )
  )

(deftest add-citation
  (let [c (:add-citation resps)
        db (res/add-citation {} [:res/add-citation c])]
    (-> db (get-in [:data :citations]) count (= 1) is)
    (-> db (get-in [:data :citations (utils/->keyword (:id c))]) nil? not is)
    )
  )

(deftest rm-citation
  (let [c (:rm-citation resps)
        db (-> {}
               (res/add-citation [:res/add-citation c])
               (res/rm-citation [:res/rm-citation c])
               )
        ]
    (-> db (get-in [:data :citations]) count (= 0) is)
    (-> db (get-in [:data :citations (utils/->keyword (:id c))]) nil? is)
    )
  )

