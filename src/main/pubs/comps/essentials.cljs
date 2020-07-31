(ns pubs.comps.essentials
  (:require [pubs.comps.authors-options :as authors-options]
            [pubs.comps.collection :as collection] 
            [pubs.comps.licenses :as licenses]
            [pubs.comps.master-types :as master-types]
            [pubs.comps.tags :as tags]
            [pubs.comps.textfield :as textfield] 
            [pubs.comps.textarea :as textarea] 
            ) 
  )

(defn- files [s k e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:req/ls-files])
  (re-frame.core/dispatch [:req/usage])
  (re-frame.core/dispatch [:panels/show k true])
  )

(defn authors [s k e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:options/authors])
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
   (collection/render s "a-content" "Content:" :content nil files)
   (collection/render s
                      "a-authors"
                      "Authors (drag to reorder):"
                      :authors-list
                      (authors-options/render s)
                      authors)

   (tags/render s)
   (licenses/render s)
   ;   (agreements s)
   ]
  )

