(ns pubs.handlers.res
  (:require [pubs.hub :as hub]
            [pubs.routes :as routes]
            [pubs.utils :as utils]
            )
  )

(defn me [db [_ res]]
  (-> db
      (assoc :loading? false)
      (assoc-in [:data :user-id] (:id res))
      )
  )

(defn master-types [db [_ res]]
  (->> res 
       (filter #(some #{(:type %)} ["File(s)" "Databases" "Series"]))
       (assoc db :master-types)
       (hub/pub)
       )
  )

(defn- pub-master-type [db pub]
  (as-> (:master-types db) $
    (group-by :id $)
    ($ (:master-type pub))
    (first $)
    (:type $)
    (assoc pub :master-type {:master-type $})
    )
  )

(defn- get-license [db]
  (if (get-in db [:data :license_type])
    (hub/license db)
    db
    )
  )

(defn pub [db [_ res]]
  (let [pub (pub-master-type db res)]
    (-> (assoc db :data pub)
        (hub/prj)
        (hub/files)
        (hub/authors)
        (hub/tags)
        (get-license)
        (hub/citations)
        (assoc :terms "I and all publication authors have read and agree to PURR terms of deposit.")
        )
    )
  )

(defn prj [db [_ res]]
  (assoc-in db [:data :prj] res)
  )

(defn files [db [_ res]]
  (update db :data merge res)
  )

(defn add-file [db [_ res file]]
  (as-> (:id res) $
    (assoc-in db [:data (:type file) (utils/->keyword $)] (assoc file :id $))
    )
  )

(defn rm-file [db [_ res k id]]
  (update-in db [:data k] dissoc (utils/->keyword id))
  )

(defn update-file [db [_ res file k]]
  (assoc-in db [:data k (utils/->keyword (:id res))] res)
  )

(defn ls-files [db [_ res]]
  (-> db
      (assoc :files res)
      (assoc-in [:ui :current-folder] [["Project files" (first (first res))]])
      )
  )

(defn usage [db [_ res]]
  (assoc db :usage res)
  )

(defn authors [db [_ res]]
  (assoc-in db [:data :authors-list] res)
  )

(defn owners [db [_ res]]
  (->> res
       (map (fn [u] [(:id u) u]))
       (into {})
       (assoc db :users)
       )
  )

(defn rm-author [db [_ res id]]
  (update-in db [:data :authors-list] dissoc (utils/->keyword id))
  )

(defn update-author [db [_ res author k]]
  (prn "UPDATE AUTHOR" res author k)
  ;(assoc-in db [:data k (utils/->keyword (:id res))] res)
  db
  )

(defn add-author [db [_ res]]
  (assoc-in db [:data :authors-list (utils/->keyword (:id res))] res)
  )

(defn new-author [db [_ res]]
  (assoc-in db [:data :authors-list (utils/->keyword (:id res))] res)
  )

(defn search-users [db [_ res]]
  (assoc db :user-results res)
  )

(defn tags [db [_ res]]
  (->>
    res
    (group-by :id)
    (map (fn [[k v]] [k (first v)]))
    (into {})
    (assoc-in db [:data :tags])
    )
  )

(defn rm-tag [db _]
  (hub/tags db)
  )

(defn add-tag [db [_ res]]
  (-> db
      (assoc-in [:ui :tag-str] "")
      (dissoc :tag-query)
      (assoc-in [:ui :tag] false)
      (hub/tags)
      ) 
  )

(defn search-tags [db [_ res]]
  (assoc db :tag-results res) 
  )

(defn licenses [db [_ res]]
  (assoc db :licenses res)
  )

(defn license [db [_ res]]
  (assoc-in db [:data :licenses] res)
  )

(defn new-pub [db [_ res pub]]
  (as-> db $
    (update $ :data merge res)
    (routes/redirect (str "/pubs/#/pubs/"
                          (get-in $ [:data :pub-id])
                          "/v/"
                          (get-in $ [:data :ver-id])
                          "/edit"
                          )
                     ) 
    )
  )

(defn save-pub [db [_ res pub]]
  (update db :data merge res)
  )

(defn citations [db [_ res]]
  (->>
    res
    (group-by :id)
    (map (fn [[k v]] [(utils/->keyword k) (first v)]))
    (into {})
    (assoc-in db [:data :citations])
    )
  )

(defn add-citation [db [_ res]]
  (assoc-in db [:data :citations (utils/->keyword (:id res))] res) 
  )

(defn search-citations [db [_ res]]
  (assoc db :doi-results res)
  )

(defn citation-types [db [_ res]]
  (assoc db :citation-types res)
  )

(defn rm-citation [db [_ res]]
  (update-in db [:data :citations] dissoc (utils/->keyword (:id res)))
  )

(defn create-citation [db [_ res]]
  ;; Scroll form, am I a dirty hack? ... yes. - JBG
  (-> js/document (.querySelector ".citations-manual .inner") (.scrollTo 0 0))
  (-> db
      (hub/add-citation res)
      (update :data dissoc :citations-manual)
      )
  )

(defn submit-pub [db [_ res]]
  (routes/redirect (str "/publications/" (get-in db [:data :pub-id])))
  db
  )

