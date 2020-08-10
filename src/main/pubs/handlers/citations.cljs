(ns pubs.handlers.citations
  (:require [pubs.hub :as hub])
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

