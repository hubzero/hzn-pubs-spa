(ns hubzero-pubs.utils
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

(defn- _date-valid? [s]
  (if (and (not (get-in @s [:ui :errors :publication-date]))
    (< (js/Date. (get-in @s [:data :publication-date])) (js/Date.)))
    (swap! s assoc-in [:ui :errors :publication-date] ["Embargo date" "can not be in the past"])
    ) 
  )

(defn valid? [s]
  (swap! s assoc-in [:ui :errors]
         (reduce (fn [errors [k v]]
                   (if (= 0 (count (get-in @s [:data k])))
                     (assoc errors k v)
                     errors
                     )
                   ) {} {:title ["Title" "can not be empty"]
                         :abstract ["Abstract" "can not be empty"]
                         :publication-date ["Embargo date" "can not be empty"]
                         :authors-list ["Authors"  "can not be empty"]
                         })
         )
  (_date-valid? s)
  (= (count (get-in @s [:ui :errors])) 0)
  )

(defn file-count [files]
  (reduce (fn [c d]
            (+ c (count (nth d 2 [])))
            ) 0 files) 
  )

(defn keywordize [m]
  (into {} (for [[k v] m] [(keyword k) v]))
  )

