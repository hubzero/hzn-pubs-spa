(ns pubs.events
  (:require
   [re-frame.core :as rf :refer [dispatch dispatch-sync]]
   [pubs.handlers.authors :as authors]
   [pubs.handlers.collection :as collection]
   [pubs.handlers.dropdown :as dropdown]
   [pubs.handlers.folders :as folders]
   [pubs.handlers.handler :as handler]
   [pubs.handlers.licenses :as licenses]
   [pubs.handlers.options :as options]
   [pubs.handlers.panels :as panels]
   [pubs.handlers.req :as req]
   [pubs.handlers.res :as res]
   [pubs.handlers.tags :as tags]
   [pubs.handlers.text :as text]
   ))

(rf/reg-event-db :actions/navigated handler/navigated)
(rf/reg-event-db :err handler/err)

;; Request/Response handlers - JBG

(rf/reg-event-db :req/me req/me)
(rf/reg-event-db :req/pub req/pub)
(rf/reg-event-db :req/master-types req/master-types)
(rf/reg-event-db :req/prj req/prj)
(rf/reg-event-db :req/files req/files)
(rf/reg-event-db :req/ls-files req/ls-files)
(rf/reg-event-db :req/add-file req/add-file)
(rf/reg-event-db :req/rm-file req/rm-file)
(rf/reg-event-db :req/usage req/usage)
(rf/reg-event-db :req/authors req/authors)
(rf/reg-event-db :req/owners req/owners)
(rf/reg-event-db :req/licenses req/licenses)
(rf/reg-event-db :req/save-pub req/save-pub)
(rf/reg-event-db :req/agree req/agree)

(rf/reg-event-db :res/me res/me)
(rf/reg-event-db :res/pub res/pub)
(rf/reg-event-db :res/master-types res/master-types)
(rf/reg-event-db :res/prj res/prj)
(rf/reg-event-db :res/files res/files)
(rf/reg-event-db :res/add-file res/add-file)
(rf/reg-event-db :res/rm-file res/rm-file)
(rf/reg-event-db :res/ls-files res/ls-files)
(rf/reg-event-db :res/usage res/usage)
(rf/reg-event-db :res/authors res/authors)
(rf/reg-event-db :res/owners res/owners)
(rf/reg-event-db :res/rm-author res/rm-author)
(rf/reg-event-db :res/update-author res/update-author)
(rf/reg-event-db :res/add-author res/add-author)
(rf/reg-event-db :res/search-users res/search-users)
(rf/reg-event-db :res/new-author res/new-author)
(rf/reg-event-db :res/tags res/tags)
(rf/reg-event-db :res/rm-tag res/rm-tag)
(rf/reg-event-db :res/add-tag res/add-tag)
(rf/reg-event-db :res/search-tags res/search-tags)
(rf/reg-event-db :res/licenses res/licenses)
(rf/reg-event-db :res/license res/license)

;; Component handlers - JBG

(rf/reg-event-db :options/show options/show)
(rf/reg-event-db :options/close options/close)
(rf/reg-event-db :options/authors options/authors)
(rf/reg-event-db :options/citations options/citations)

(rf/reg-event-db :dropdown/show dropdown/show)
(rf/reg-event-db :dropdown/change dropdown/change)
(rf/reg-event-db :dropdown/click dropdown/click)
(rf/reg-event-db :dropdown/rm dropdown/rm)

(rf/reg-event-db :text/change text/change)

(rf/reg-event-db :collection/order collection/order)

(rf/reg-event-db :panels/show panels/show)
(rf/reg-event-db :panels/close panels/close)
(rf/reg-event-db :panels/pop panels/!pop)
(rf/reg-event-db :panels/push panels/push)
(rf/reg-event-db :panels/field panels/field)

(rf/reg-event-db :folders/pop folders/!pop)
(rf/reg-event-db :folders/push folders/push)

(rf/reg-event-db :authors/edit authors/edit)
(rf/reg-event-db :authors/name authors/fillname)
(rf/reg-event-db :authors/rm authors/rm)
(rf/reg-event-db :authors/poc authors/poc)
(rf/reg-event-db :authors/add authors/add)
(rf/reg-event-db :authors/search authors/search)
(rf/reg-event-db :authors/result authors/result)
(rf/reg-event-db :authors/upsert authors/upsert)
(rf/reg-event-db :authors/modify authors/modify)
(rf/reg-event-db :authors/order authors/order)

(rf/reg-event-db :tags/creator tags/creator)
(rf/reg-event-db :tags/rm tags/rm)
(rf/reg-event-db :tags/add tags/add)
(rf/reg-event-db :tags/search tags/search)

(rf/reg-event-db :licenses/ack licenses/ack)
(rf/reg-event-db :licenses/select licenses/select)

