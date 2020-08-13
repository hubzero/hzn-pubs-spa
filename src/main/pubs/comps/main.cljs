(ns pubs.comps.main
  (:require [pubs.comps.navigation :as nav]
            [pubs.comps.form :as form]
            [pubs.comps.aside-buttons :as aside-buttons]
            )
  )

(defn render [s]
  [:div {:class (concat [:page :page-main :--remove]
                        (if (get-in s [:ui :summary]) [:hide :remove]))}
   (nav/render s) 
   (form/render s)
   (aside-buttons/render s)
   ]
  )

