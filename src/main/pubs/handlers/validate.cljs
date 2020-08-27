(ns pubs.handlers.validate)

(defn- date [db errors]
  (if (and (get-in db [:data :publication-date])
           (< (js/Date. (get-in db [:data :publication-date])) (js/Date.)))
    (assoc errors :publication-date ["Embargo date" "can not be in the past"])
    errors
    )
  )

(defn- ack [db errors]
  (if (not (get-in db [:data :ack]))
    (assoc errors :ack ["Acknowledgement" "is required"])
    errors
    )
  )

(defn- terms [db errors]
  (if (not (get-in db [:data :terms])) 
    (assoc errors :terms ["Terms" "must be agreed to"])
    errors
    )
  )

(defn- poc [db errors]
  ;; If we already have validation errors with authors, roll w/ those - JBG
  (if (:authors-list errors)
    errors
    (let [poc? (reduce (fn [poc? [id a]]
                         (or poc? (> (:poc a) 0))
                         ) false (get-in db [:data :authors-list]))]
      (if (not poc?)
        (assoc errors :authors-list ["Point of contact" "is required"])
        errors
        )
      )
    )
  )

(defn- form [db errors]
  (reduce (fn [errors [k v]]
            (if (= 0 (count (get-in db [:data k])))
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
  )

(defn pub [db]
  (->> (form db {})
       (date db) 
       (ack db)
       (terms db)
       (poc db)
       )
  )

(defn- year [y errors]
  (prn "CIT YEAR" errors)
  (as-> y $
    (if (not (nil? $)) (clojure.string/trim $) "")
    (if (re-matches #"^(19|20)\d{2}$" $)
      errors
      (assoc errors :year ["Year" "must be valid 4 digits"])
      )
    )
  )

(defn submitted? [db]
  (-> (get-in db [:data :state])
      (= 3)
      (not)
      )
  )

(defn valid? [db]
  (-> (pub db) count (= 0)) 
  )
 
(defn- citation [db]
  (as-> db $
    (reduce (fn [errors [k v]]
              (if (= 0 (count (get-in $ [:data :citations-manual k])))
                (assoc errors k v)
                errors
                )
              ) {} {:type ["Type" "can not be empty"]
                    :title ["Title" "can not be empty"]
                    :year ["Year" "can not be empty"]
                    })
    (year (get-in db [:data :citations-manual :year]) $)
    (assoc-in db [:ui :errors] $)
    )
  )
 
