(ns hzn-pubs-spa.utils
  )

(defn find-ancestor [el sel]
  (if el
    (if (.matches el sel) el (find-ancestor el.parentElement sel))
    )
  )

;(defn keyword-to-int [k]
;  (if (keyword? k) (js/parseInt (name k)) k)
;  )


(defn ->int [k]
  (cond
    (integer? k) k
    (keyword? k) (->> k name js/parseInt)
    :else (js/parseInt k)
    )
  )

(defn ->keyword [i]
  (cond
    (integer? i) (->> i str keyword)
    :else (keyword i)
    )
  )

(defn author-key [a]
  (str (:id a) "_"
       (:user_id a) "_"
       (or (:name a) (str (:firstname a) "_" (:lastname a)))  "_"
       (:organization a))
  )

(defn format-citation [c]
  (if (and (:formatted c) (> (count (:formatted c)) 0)) 
    (:formatted c) 
    (str (if (:author c) (str (:author c) ". "))
         (if (:year c) (str (:year c) ". "))
         (if (:title c) (str (:title c) ". "))
         (if (:journal c) (str (:journal c) ", " ))
         (if (:volume c) (str (:volume c) ", "))
         (if (:pages c) (str (:pages c) ". "))
         (if (:doi c) (str "doi:" (:doi c)))
         ) 
    )
  )

(defn savable? [s]
  (-> 
    (and (get-in @s [:data :prj-id] false)) 
    (and (get-in @s [:data :user-id] false)) 
    )
  )

(defn email-valid? [a errors]
  (as-> (:email a) $
    (clojure.string/trim $)
    (if (or (= (count $) 0) (re-matches #".+\@.+\..+" $))
      errors
      (assoc errors :email ["Email" "must be valid"])
      )
    )
  )

(defn year-valid? [c errors]
  (as-> (:year c) $
    (if (not (nil? $)) (clojure.string/trim $) "")
    (if (re-matches #"^(19|20)\d{2}$" $)
      errors
      (assoc errors :year ["Year" "must be valid 4 digits"])
      )
    )
  )

(defn fields-valid? [a fields errors]
  (reduce (fn [errors [k v]]
            (as-> (k a) $
              (if $ (clojure.string/trim $) $)
              (if (= 0 (count $)) (assoc errors k v) errors)
              )
            ) errors fields)
  )

(defn authors-new-valid? [s]
  (let [a (get-in @s [:data :authors-new])]
    (->> {}
         (fields-valid? a {:firstname ["Firstname" "can not be empty"]
                           :lastname ["Lastname" "can not be empty"]
                           })
         (email-valid? a)
         (swap! s assoc-in [:ui :errors]) 
         )
    )
  (= (count (get-in @s [:ui :errors])) 0)
  )

(defn citations-manual-valid? [s]
  (let [c (get-in @s [:data :citations-manual])]
    (->> {}
         (fields-valid? c {:type ["Type" "can not be empty"]
                           :title ["Title" "can not be empty"]
                           :year ["Year" "can not be empty"]
                           })
         (year-valid? c)
         (swap! s assoc-in [:ui :errors]) 
         )
    )
  (= (count (get-in @s [:ui :errors])) 0)
  )

(defn- _date-valid? [s errors]
  (if (and (get-in @s [:data :publication-date])
    (< (js/Date. (get-in @s [:data :publication-date])) (js/Date.)))
    (assoc errors :publication-date ["Embargo date" "can not be in the past"])
    errors
    ) 
  )

(defn- _terms-valid? [s errors]
  (if (not (get-in @s [:data :terms])) 
    (assoc errors :terms ["Terms" "must be agreed to"])
    errors
    ) 
  )

(defn- _ack-valid? [s errors]
  (if (not (get-in @s [:data :ack]))
    (assoc errors :ack ["Acknowledgement" "is required"])
    errors
    )
  )

(defn- poc-required [s errors]
  ;; If we already have validation errors with authors, roll w/ those - JBG
  (if (:authors-list errors)
    errors
    (let [poc? (reduce (fn [poc? [id a]]
                         (or poc? (> (:poc a) 0))
                         ) false (get-in @s [:data :authors-list]))]
      (if (not poc?)
        (assoc errors :authors-list ["Point of contact" "is required"])
        errors
        )
      )  
    )
  )

(defn- _errors [s]
  (->>
    (reduce (fn [errors [k v]]
              (if (= 0 (count (get-in @s [:data k])))
                (assoc errors k v)
                errors
                )
              ) {} {:title ["Title" "can not be empty"]
                    :abstract ["Abstract" "can not be empty"]
                    :authors-list ["Authors"  "can not be empty"]
                    :content ["Content" "can not be empty"]
                    :tags ["Tags" "can not be empty"]
                    :licenses ["Licenses" "can not be empty"]
                    })
    (_date-valid? s)
    (_ack-valid? s)
    (poc-required s)
    )
  )

(defn errors? [s]
  (let [errors (->> (_errors s) (_terms-valid? s))]
    (swap! s assoc-in [:ui :errors] errors)
    (prn "ERRORS" errors)
    (-> errors (count) (= 0))
    )
  )

(defn valid? [s]
  (-> (_errors s) (count) (= 0)))

(defn file-count [files]
  (reduce (fn [c d]
            (+ c (count (nth d 2 [])))
            ) 0 files) 
  )

(defn keywordize [m]
  (into {} (for [[k v] m] [(keyword k) v]))
  )

(defn fillname [v]
  (assoc v
         :firstname (or (:firstname v) (first (clojure.string/split (:fullname v) #" ")))
         :lastname (or (:lastname v) (last (clojure.string/split (:fullname v) #" "))))
  )

(defn water-citation [s c]
  (swap! s assoc-in [:data :citations-manual]
         (-> c
             (assoc :book (:booktitle c))
             )
         )
  )

