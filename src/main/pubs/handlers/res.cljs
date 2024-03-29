(ns pubs.handlers.res
  (:require [pubs.hub :as hub]
            [pubs.routes :as routes]
            [pubs.utils :as utils]
            [clojure.string :as s])
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

(defn- content [db]
  (case (get-in db [:data :master-type :master-type])
    "Databases" (hub/dbs db)
    "Series" (hub/series db)
    (hub/files db)
    )
  )

(defn pub [db [_ res]]
  (let [pub (pub-master-type db res)]
    (-> (assoc db :data pub)
        (hub/prj)
        (content)
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

(defn update-file [db [_ res]]
  (assoc-in db [:data (:type res) (utils/->keyword (:id res))] res)
  )

(defn image-file-filter [filename]
  (re-find #"\.(jpg|gif|jpeg|png|bmp|tif|tiff)$" (s/lower-case filename)))

(defn image-files-only
  [res]
  (map (fn [dir-set]
         ;; first is directory name string; last is list of files to filter over
         (concat (butlast dir-set)
                 [(filter image-file-filter (last dir-set))]))
       res))

(defn ls-files
  "`res` looks like ^
  "
  [db [_ res]]
  ;; Do a quick filtering on the files if we're looking at images.
  (let [fres (if (= (:ls-type-kw db) :images)
               (image-files-only res)
               res)]
    (-> db
        (assoc :files fres)
        (assoc-in [:files-m :root] (ffirst fres))
        (assoc-in [:files-m :location] [(ffirst fres)])
        (assoc-in [:files-m :files] (reduce
                                      (fn [m [fullpath subdirs files]]
                                        (assoc m fullpath {:subdirs subdirs :files files})
                                        )
                                      {}
                                      fres))
        (assoc-in [:ui :current-folder] [["Project files" (first (first res))]])
        )))

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
  ;(update-in db [:data :authors-list] dissoc (utils/->keyword id))
  (hub/authors db)
  )

(defn update-author [db [_ res]]
  (assoc-in db [:data :authors-list (utils/->keyword (:id res))] res)
  )

(defn add-author [db [_ res]]
  ;(assoc-in db [:data :authors-list (utils/->keyword (:id res))] res)
  (hub/authors db)
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
  (let [project-id     (get-in db [:data :prj-id])
        publication-id (get-in db [:data :pub-id])]
    (routes/redirect (str "/projects/" project-id "/publications/" publication-id)))
  db
  )

(defn ls-dbs [db [_ res]]
  (assoc db :databases res)
  )

(defn dbs [db [_ res]]
  (->> res
       (map (fn [database] [(:id database) database]))
       (into {})
       (assoc-in db [:data :databases])
       )
  )

(defn add-db [db [_ res database]]
  (as-> (:id res) $
    (assoc-in db [:data :databases (:id res)] res)
    )
  )

(defn rm-db [db [_ res k id]]
  (update-in db [:data k] dissoc id)
  )

(defn update-db [db [_ res]]
  (assoc-in db [:data (:type res) (:id res)] res)
  )

(defn search-series [db [_ res]]
  (assoc db :pubs res)
  )

(defn series [db [_ res]]
  (->> res
       (map (fn [series] [(:id series) series]))
       (into {})
       (assoc-in db [:data :series])
       )
  )

(defn add-series [db [_ res pub]]
  (as-> (:id res) $
    (assoc-in db [:data :series (:id res)] res)
    )
  )

(defn rm-series [db [_ res k id]]
  (update-in db [:data k] dissoc id)
  )

(defn update-series [db [_ res]]
  (assoc-in db [:data (:type res) (:id res)] res)
  )


