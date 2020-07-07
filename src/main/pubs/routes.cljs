(ns pubs.routes
  (:require
    [reagent.core :as reagent]
    [reagent.dom :as rdom]
    [re-frame.core :as rf :refer [dispatch dispatch-sync]]
    [reitit.frontend :as rfe]
    [reitit.frontend.easy :as rfez :refer [href]]
    [reitit.coercion.spec :as rss]
    [pubs.controllers :as controllers]
    [pubs.comps.app :as app]
    ))

;; Triggering navigation from events.
;; https://github.com/day8/re-frame/blob/master/docs/Effects.md
(rf/reg-fx
  :pubs.core/navigate!
  (fn [route]
    (apply rfez/push-state route)))

(def routes
  [["/pubs/:pub-id/v/:ver-id" {:name :version
                               :view app/render
                               :controller controllers/version 
                               }]
   ]
  )

(def router (rfe/router routes))

(defn on-navigate [new-match]
  (when new-match
    (dispatch [:actions/navigated new-match])))

(defn init! []
  (rf/clear-subscription-cache!)
  (prn "Initializing routes")
  ; registers event listeners on HTML5 history and hashchange events
  (rfez/start! router
               on-navigate
               {:use-fragment true})
  (rdom/render [app/render]
               (.getElementById js/document "app")))

