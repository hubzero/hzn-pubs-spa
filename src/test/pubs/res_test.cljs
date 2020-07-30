(ns pubs.res-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [pubs.handlers.res :as res]
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

(deftest rm-author 
  (let [db (-> (res/authors {} [:res/authors (:authors resps)])
               (res/rm-author [:res/rm-author (:rm-author resps) 456]) )]
    (-> db (get-in [:data :authors-list]) nil? not is)
    (-> db (get-in [:data :authors-list]) count (= 3) is)
    )
  )

(deftest update-author 
  (let [db (-> (res/authors {} [:res/authors (:authors resps)])
               (res/update-author [:res/update-author (:update-author resps)]) )]
    (-> db (get-in [:data :authors-list]) nil? not is)
    (-> db (get-in [:data :authors-list]) count (= 4) is)
    )
  )

(deftest add-author 
  (let [db (-> (res/authors {} [:res/authors (:authors resps)])
               (res/add-author [:res/add-author (:add-author resps)]) )]
    (-> db (get-in [:data :authors-list]) nil? not is)
    (-> db (get-in [:data :authors-list]) count (= 5) is)
    )
  )

(deftest search-users 
  (let [db (res/search-users {} [:res/search-users (:search-users resps)]) ]
    (-> db :user-results count (= 1) is)  
    )
  )


