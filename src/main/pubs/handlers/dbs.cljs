(ns pubs.handlers.dbs
  (:require [pubs.handlers.validate :as validate]
            [pubs.hub :as hub]
            [pubs.utils :as utils]
            )
  )

(defn ls [db _]
  (hub/ls-dbs db)
  )

(defn dbs [db _]
  (hub/dbs db)
  )

(defn add [db [_ database]]
  (hub/add-db db database)
  )

(defn rm [db [_ k database-id]]
  (hub/rm-db db k database-id)
  )

(defn edit [db [_ k attach]]
  (hub/update-db db k attach)
  )

