(ns pubs.events
  (:require
   [re-frame.core :as rf :refer [dispatch dispatch-sync]]
   [pubs.handler :as handler]
   [pubs.req :as req]
   [pubs.res :as res]
   ))

(rf/reg-event-db ::initialize-db handler/initialize-db)
(rf/reg-event-db :actions/navigated handler/navigated)
(rf/reg-event-db :err handler/err)

(rf/reg-event-db :req/me req/me)
(rf/reg-event-db :req/pub req/pub)
(rf/reg-event-db :req/master-types req/master-types)

(rf/reg-event-db :res/me res/me)
(rf/reg-event-db :res/pub res/pub)
(rf/reg-event-db :res/master-types res/master-types)

