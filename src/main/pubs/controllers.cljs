(ns pubs.controllers
  (:require [re-frame.core :as rf :refer [dispatch dispatch-sync]]) 
  )

(defn pub [db params]
  ;(dispatch [:req/master-types params])
  (dispatch [:req/pub params])
  ;(dispatch [:req/me])
  )

