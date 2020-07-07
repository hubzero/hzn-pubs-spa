(ns pubs.controllers
  (:require [re-frame.core :as rf :refer [dispatch dispatch-sync]]) 
  )

(defn version [db params]
  (dispatch [:req/master-types params])
  ;(dispatch [:req/pub params])
  ;(dispatch [:req/me])
  db
  )

