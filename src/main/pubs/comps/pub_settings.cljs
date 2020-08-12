(ns pubs.comps.pub-settings
  (:require [pubs.comps.pub-date :as pub-date]
    [pubs.comps.textarea :as textarea]) 
  )

(defn render [s]
  [:fieldset.fieldset-section
   [:header#a-pub-settings.anchor.a-header [:legend "Publish Settings"]]
   (pub-date/render s)
   (textarea/render s "a-comments" "Comments to the administrator:" "comments")
   ]
  )
 
