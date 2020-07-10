(ns pubs.events
  (:require
   [re-frame.core :as rf :refer [dispatch dispatch-sync]]
   [pubs.handlers.handler :as handler]
   [pubs.handlers.req :as req]
   [pubs.handlers.res :as res]
   [pubs.handlers.dropdown :as dropdown]
   ))

(rf/reg-event-db ::initialize-db handler/initialize-db)
(rf/reg-event-db :actions/navigated handler/navigated)
(rf/reg-event-db :err handler/err)

(rf/reg-event-db :req/me req/me)
(rf/reg-event-db :req/pub req/pub)
(rf/reg-event-db :req/master-types req/master-types)
(rf/reg-event-db :req/prj req/prj)

(rf/reg-event-db :res/me res/me)
(rf/reg-event-db :res/pub res/pub)
(rf/reg-event-db :res/master-types res/master-types)
(rf/reg-event-db :res/prj res/prj)

(rf/reg-event-db :dropdown/show dropdown/show)
(rf/reg-event-db :dropdown/change dropdown/change)
(rf/reg-event-db :dropdown/click dropdown/click)
(rf/reg-event-db :dropdown/rm dropdown/rm)

