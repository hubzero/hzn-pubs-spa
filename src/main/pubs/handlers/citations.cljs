(ns pubs.handlers.citations
  (:require [pubs.hub :as hub]
            [pubs.utils :as utils]
            )
  )

(defn search [db [_ v]]
  (as-> db $
      (if (> (count v) 0)
        (hub/search-citations $ v)
        (dissoc $ :doi-results)
        )
      (assoc $ :doi-query v)
      ) 
  )

(defn add [db [_ c]]
  (hub/add-citation db c)
  )

(defn create [db [_ c]]
  (as-> db $
    ;; If it has an id, it's from our DB so just try it to the pub
    ;; No id? probably from doi.org, will need to be added to our DB
    (if (:id c)
      (hub/add-citation $ c)
      (-> $ 
          (assoc-in [:data :citations-manual] c)
          (hub/create-citation c)
          )
      )    
    (assoc $ :doi-query "")
    (dissoc $ :doi-results)
    )
  )

(defn options [db [_ id]]
  (assoc-in db [:ui :options :citation id] true)
  )

(defn text [db [_ k f v]]
  (assoc-in db [:data k (:name f)] v)
  )

(defn- errors [db]
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
    (utils/year-valid? (get-in db [:data :citations-manual :year]) $)
    (assoc-in db [:ui :errors] $)
    )
  )

(defn manual [db _]
  (as-> (errors db) $
    (if (= (count (get-in $ [:ui :errors])) 0)
      (do
        (re-frame.core/dispatch [:panels/close])
        (hub/create-citation $ (get-in $ [:data :citations-manual]))   
        )
      $
      )
    )
  )


