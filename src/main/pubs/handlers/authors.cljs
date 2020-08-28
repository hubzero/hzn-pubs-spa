(ns pubs.handlers.authors
  (:require [pubs.hub :as hub]
            [pubs.utils :as utils])
  )

(defn edit [db [_ create?]]
  (-> db
      (assoc-in [:ui :panels :authors-new] true)
      (assoc-in [:ui :author-options :is-new] create?)
      )
  )

(defn fillname [db [_ v]]
  (assoc-in db [:data :authors-new] (utils/fillname v)) 
  )

(defn rm [db [_ id]]
  (hub/rm-author db (utils/->int id))
  )

(defn poc [db [_ id poc?]]
  (as-> (get-in db [:data :authors-list id]) $
    (assoc $ :poc poc?)
    (hub/update-author db $ :authors-list)
    )
  )

(defn add [db [_ author]]
  (hub/add-author db author)
  )

(defn search [db [_ v]]
  (as-> db $
    (assoc $ :user-query v)
    (if (not (empty? v))
      (hub/search-users $ v)
      ) 
    )
  )

(defn result [db [_ k v]]
  (-> db
      (assoc-in [:data k] {:firstname (str (:givenname v) " " (:middlename v))
                           :lastname (:surname v)
                           :organization (:org v)
                           :email (:email v)
                           :id (:id v 0)
                           :name (:name v)
                           }) 
      (assoc :user-query "")
      (assoc :user-results nil)
      )
  )

(defn modify [db [_ u]]
  (hub/update-author db u :authors-list)
  )

(defn email-valid? [a errors]
  (as-> (or (:email a) "") $
    (clojure.string/trim $)
    (if (or (= (count $) 0) (re-matches #".+\@.+\..+" $))
      errors
      (assoc errors :email ["Email" "must be valid"])
      )
    )
  )

(defn names-valid? [a errors]
  (reduce (fn [errors [k v]]
            (as-> (k a) $
              (if $ (clojure.string/trim $) $)
              (if (= 0 (count $)) (assoc errors k v) errors)
              )
            ) errors {:firstname ["Firstname" "can not be empty"]
                      :lastname ["Lastname" "can not be empty"]
                      })
  )

(defn upsert [db [_ k]]
  (let [a (get-in db [:data :authors-new])
        errors (->> {} (names-valid? a) (email-valid? a))
        new? (get-in db [:ui :author-options :is-new])
        u (as-> (get-in db [:data k]) $
            (assoc $ :fullname (str (:firstname $) " " (:lastname $)))
            )
        ]
    (if (= (count errors) 0)
      (if new?
        (hub/new-author db u)
        (hub/update-author db u :authors-list)
        )
      )
    (assoc-in db [:ui :errors] errors)
    )
  )

(defn order [db [_ k l]]
  (assoc-in db [:data k] l)
  )

