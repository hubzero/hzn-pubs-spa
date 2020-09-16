(ns pubs.comps.essentials
  (:require [pubs.comps.agreements :as agreements]
            [pubs.comps.authors-options :as authors-options]
            [pubs.comps.collection :as collection] 
            [pubs.comps.licenses :as licenses]
            [pubs.comps.master-types :as master-types]
            [pubs.comps.tags :as tags]
            [pubs.comps.textfield :as textfield] 
            [pubs.comps.textarea :as textarea]
            )
  )

(defn authors [s k e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:options/authors])
  )

(defn- content-type [s]
  (case (get-in s [:data :master-type :master-type])
    "Databases" collection/dbs
    "Series" collection/series
    collection/files 
    )
  )

(defn- panel-type [s]
  (case (get-in s [:data :master-type :master-type])
    "Databases" :databases
    "Series" :series
    :content
    )
  )

(defn render [s]
  [:fieldset.fieldset-section
   [:header
    [:legend "Essentials"]
    [:div.note "all fields required"]
    ]
   (master-types/render s)
   (textfield/render s "a-title" "Title:" :title)
   (textarea/render s "a-abstract" "Abstract:" :abstract)
   ;(textarea/render s "a-description" "Description:" :description)
   (collection/render s
                      "a-content"
                      "Content (drag to reorder):"
                      (panel-type s)
                      nil
                      (content-type s))
   (collection/render s
                      "a-authors"
                      "Authors (drag to reorder):"
                      :authors-list
                      (authors-options/render s)
                      authors)

   (tags/render s)
   (licenses/render s)
   (agreements/render s)
   ]
  )

