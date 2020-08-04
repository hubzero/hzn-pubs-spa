(ns pubs.comps.additional-details
  (:require [pubs.comps.collection :as collection] 
    
    )
  )

(defn render [s]
  [:fieldset.fieldset-section
   [:header [:legend "Additional Details"]]
   (collection/render s "a-image-gallery" "Image gallery:" :images nil collection/files)
   ;(textfield s "a-url" "External website URL:" "url")
   ;(collection s "a-docs" "Supporting docs:" :support-docs nil handle-files-options)
   ;(collection s "a-citations" "Citations:" :citations (options/citations s) handle-citation-options)
   ;(textarea s "a-verion-notes" "Version release notes:" "release-notes")
   ]
  )
 
