(ns pubs.comps.additional-details
  (:require [pubs.comps.citations-options :as co]
            [pubs.comps.collection :as collection] 
            [pubs.comps.textarea :as textarea]
            [pubs.comps.textfield :as textfield]
            )
  )

(defn citations [s k e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:options/citations])
  )

(defn render [s]
  [:fieldset.fieldset-section
   [:header [:legend "Additional Details"]]
   (collection/render s "a-image-gallery" "Image gallery:" :images nil collection/files)
   (textfield/render s "a-url" "External website URL:" :url)
   (collection/render s "a-docs" "Supporting docs:" :support-docs nil collection/files)
   (collection/render s
                      "a-citations"
                      "Citations:"
                      :citations
                      (co/render s)
                      citations)
   (textarea/render s "a-verion-notes" "Version release notes:" :release-notes)
   ]
  )
 
