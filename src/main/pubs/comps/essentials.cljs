(ns pubs.comps.essentials
  (:require [pubs.comps.agreements :as agreements]
            [pubs.comps.authors-options :as authors-options]
            [pubs.comps.collection :as collection] 
            [pubs.comps.editor :as editor]
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

(defn render [s]
  (prn "ESSENTIALS" (keys s))
  [:fieldset.fieldset-section
   [:header
    [:legend "Essentials"]
    [:div.note "all fields required"]
    ]
   (master-types/render s)
   (textfield/render s "a-title" "Title:" :title)
   (textarea/render s "a-abstract" "Abstract:" :abstract)
   (editor/render s)
   (collection/render s "a-content" "Content:" :content nil collection/files)
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

