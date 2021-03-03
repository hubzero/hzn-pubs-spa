(ns pubs.routes
  (:require
    [reagent.core :as reagent]
    [reagent.dom :as rdom]
    [re-frame.core :as rf :refer [dispatch dispatch-sync]]
    [reitit.frontend :as rfe]
    [reitit.frontend.easy :as rfez :refer [href]]
    [reitit.coercion.spec :as rss]
    [pubs.controllers :as controllers]
    [pubs.containers.app :as app]
    [pubs.containers.summary :as summary]
    ))

;; Triggering navigation from events.
;; https://github.com/day8/re-frame/blob/master/docs/Effects.md
(rf/reg-fx
  :pubs.core/navigate!
  (fn [route]
    (apply rfez/push-state route)))

(def routes
  [["/pubs/:pub-id/v/:ver-id" {:name :summary
                               :view summary/render
                               :controllers [{:start controllers/summary}]
                               }]
   ["/pubs/:pub-id/v/:ver-id/edit" {:name :pub
                                    :view app/render
                                    :controllers [{:start controllers/pub}]
                                    }]
   ["/prjs/:id" {:name :prj
                 :view app/render
                 :controllers [{:start controllers/prj}]
                 }]
   ]
  )

(def router (rfe/router routes))

(defn redirect [url]
  (->
    js/window
    .-location
    (set! url)
    )
  )

(defn on-navigate [new-match]
  (when new-match
    (dispatch [:actions/navigated new-match])))

(defn init! []
  (rf/clear-subscription-cache!)
  ; registers event listeners on HTML5 history and hashchange events
  (rfez/start! router
               on-navigate
               {:use-fragment true})
  (rdom/render [app/render]
               (.getElementById js/document "app")))

