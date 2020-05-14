(ns hzn-pubs-spa.utils
  )

(defn find-ancestor [el sel]
  (if el
    (if (.matches el sel) el (find-ancestor el.parentElement sel))
    )
  )

(defn author-key [a]
  (str (:id a) "_"
       (:user_id a) "_"
       (or (:name a) (str (:firstname a) "_" (:lastname a)))  "_"
       (:organization a))
  )

(defn format-citation [c]
  (str (:author c) ". "
       (:year c) ". "
       (:title c) ". "
       (:journal c) ", "
       (:volume c) ", "
       (:pages c) ". "
       "doi:" (:doi c)
       ) 
  )

(defn savable? [s]
  (-> 
    (and (get-in @s [:data :prj-id] false)) 
    (and (get-in @s [:data :user-id] false)) 
    )
  )

(defn authors-new-valid? [s]
  (swap! s assoc-in [:ui :errors]
         (reduce (fn [errors [k v]]
                   (if (= 0 (count (get-in @s [:data :authors-new k])))
                     (assoc errors k v)
                     errors
                     )
                   ) {} {:firstname ["Firstname" "can not be empty"]
                         :lastname ["Lastname" "can not be empty"]
                         })
         )
  (= (count (get-in @s [:ui :errors])) 0)
  )

(defn citations-manual-valid? [s]
  (prn "ASFDSF citations-manual-valid?")
  (swap! s assoc-in [:ui :errors]
         (reduce (fn [errors [k v]]
                   (if (= 0 (count (get-in @s [:data :citations-manual k])))
                     (assoc errors k v)
                     errors
                     )
                   ) {} {:citation-type ["Type" "can not be empty"]
                         :title ["Title" "can not be empty"]
                         })
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
    )
  )

(defn errors? [s]
  (let [errors (->> (_errors s) (_terms-valid? s))]
    (swap! s assoc-in [:ui :errors] errors)
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

