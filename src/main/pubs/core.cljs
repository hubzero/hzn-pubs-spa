(ns pubs.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as rf]
   [pubs.config :as config]
   [pubs.routes :as rt]
   [pubs.events :as events]
   ))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/before-load stop []
  (prn "stop"))

(defn ^:dev/after-load start []
  (rf/clear-subscription-cache!)
  (rt/init!))

(defn init []
  (rf/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (-> js/document (.addEventListener "DOMContentLoaded" rt/init!))
  )

