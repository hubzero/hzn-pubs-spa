(ns pubs.controllers
  (:require [re-frame.core :as rf :refer [dispatch dispatch-sync]]) 
  )

(defn pub [db params]
  (dispatch [:req/pub params])
  )

(defn prj [db params]
  (dispatch [:req/new params])
  )

