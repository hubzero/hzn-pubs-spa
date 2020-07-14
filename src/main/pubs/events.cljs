(ns pubs.events
  (:require
   [re-frame.core :as rf :refer [dispatch dispatch-sync]]
   [pubs.handlers.collection :as collection]
   [pubs.handlers.dropdown :as dropdown]
   [pubs.handlers.file :as file]
   [pubs.handlers.handler :as handler]
   [pubs.handlers.options :as options]
   [pubs.handlers.panels :as panels]
   [pubs.handlers.req :as req]
   [pubs.handlers.res :as res]
   [pubs.handlers.text :as text]
   ))

(rf/reg-event-db ::initialize-db handler/initialize-db)
(rf/reg-event-db :actions/navigated handler/navigated)
(rf/reg-event-db :err handler/err)

;; Request/Response handlers - JBG

(rf/reg-event-db :req/me req/me)
(rf/reg-event-db :req/pub req/pub)
(rf/reg-event-db :req/master-types req/master-types)
(rf/reg-event-db :req/prj req/prj)
(rf/reg-event-db :req/files req/files)
(rf/reg-event-db :req/ls-files req/ls-files)

(rf/reg-event-db :res/me res/me)
(rf/reg-event-db :res/pub res/pub)
(rf/reg-event-db :res/master-types res/master-types)
(rf/reg-event-db :res/prj res/prj)
(rf/reg-event-db :res/files res/files)
(rf/reg-event-db :res/rm-file res/rm-file)

;; Component handlers - JBG

(rf/reg-event-db :file/rm file/rm)

(rf/reg-event-db :options/show options/show)
(rf/reg-event-db :options/close options/close)

(rf/reg-event-db :dropdown/show dropdown/show)
(rf/reg-event-db :dropdown/change dropdown/change)
(rf/reg-event-db :dropdown/click dropdown/click)
(rf/reg-event-db :dropdown/rm dropdown/rm)

(rf/reg-event-db :text/change text/change)

(rf/reg-event-db :collection/order collection/order)

(rf/reg-event-db :panels/show panels/show)
(rf/reg-event-db :panels/close panels/close)

