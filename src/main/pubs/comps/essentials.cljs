(ns pubs.comps.essentials
  (:require [pubs.comps.master-types :as master-types]
            [pubs.comps.textfield :as textfield] 
            [pubs.comps.textarea :as textarea] 
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

   ;   (collection s "a-content" "Content:" :content nil handle-files-options)
   ;   (collection s "a-authors" "Authors (drag to reorder):" :authors-list (options/authors s) handle-author-options)
   ;   (tags/tags s)
   ;   (licenses s)
   ;   (agreements s)
   ]
  )

