(ns pubs.utils)

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

(defn year-valid? [year errors]
  (as-> year $
    (if (not (nil? $)) (clojure.string/trim $) "")
    (if (re-matches #"^(19|20)\d{2}$" $)
      errors
      (assoc errors :year ["Year" "must be valid 4 digits"])
      )
    )
  )

;(defn savable? [s]
;  (-> 
;    (and (get-in @s [:data :prj-id] false)) 
;    (and (get-in @s [:data :user-id] false)) 
;    )
;  )
;

;(defn citations-manual-valid? [s]
;  (swap! s assoc-in [:ui :errors]
;         (reduce (fn [errors [k v]]
;                   (if (= 0 (count (get-in @s [:data :citations-manual k])))
;                     (assoc errors k v)
;                     errors
;                     )
;                   ) {} {:citation-type ["Type" "can not be empty"]
;                         :title ["Title" "can not be empty"]
;                         })
;         )
;  (= (count (get-in @s [:ui :errors])) 0)
;  )
; 
;(defn- _date-valid? [s errors]
;  (if (and (get-in @s [:data :publication-date])
;    (< (js/Date. (get-in @s [:data :publication-date])) (js/Date.)))
;    (assoc errors :publication-date ["Embargo date" "can not be in the past"])
;    errors
;    ) 
;  )
;
;(defn- _terms-valid? [s errors]
;  (if (not (get-in @s [:data :terms])) 
;    (assoc errors :terms ["Terms" "must be agreed to"])
;    errors
;    ) 
;  )
;
;(defn- _ack-valid? [s errors]
;  (if (not (get-in @s [:data :ack]))
;    (assoc errors :ack ["Acknowledgement" "is required"])
;    errors
;    )
;  )
;
;(defn- _errors [s]
;  (->>
;    (reduce (fn [errors [k v]]
;              (if (= 0 (count (get-in @s [:data k])))
;                (assoc errors k v)
;                errors
;                )
;              ) {} {:title ["Title" "can not be empty"]
;                    :abstract ["Abstract" "can not be empty"]
;                    :authors-list ["Authors"  "can not be empty"]
;                    :content ["Content" "can not be empty"]
;                    :tags ["Tags" "can not be empty"]
;                    :licenses ["Licenses" "can not be empty"]
;                    })   
;    (_date-valid? s)
;    (_ack-valid? s) 
;    )
;  )
;
;(defn errors? [s]
;  (let [errors (->> (_errors s) (_terms-valid? s))]
;    (swap! s assoc-in [:ui :errors] errors)
;    (-> errors (count) (= 0))
;    )
;  )
;
;(defn valid? [s]
;  (-> (_errors s) (count) (= 0)))

(defn file-count [files]
  (reduce (fn [c d]
            (+ c (count (nth d 2 [])))
            ) 0 files) 
  )

(defn keywordize [m]
  (into {} (for [[k v] m] [(keyword k) v]))
  )

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

(defn fillname [v]
  (assoc v
         :firstname (or (:firstname v) (first (clojure.string/split (:fullname v) #" ")))
         :lastname (or (:lastname v) (last (clojure.string/split (:fullname v) #" "))))
  )

;(defn water-citation [s c]
;  (swap! s assoc-in [:data :citations-manual]
;         (-> c
;             (assoc :book (:booktitle c))
;             )
;         )
;  )
; 
