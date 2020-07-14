(ns pubs.comps.essentials
  (:require [pubs.comps.collection :as collection] 
            [pubs.comps.master-types :as master-types]
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
   ;   (collection s "a-authors" "Authors (drag to reorder):" :authors-list (options/authors s) handle-author-options)
   ;   (tags/tags s)
   ;   (licenses s)
   ;   (agreements s)
   ]
  )

