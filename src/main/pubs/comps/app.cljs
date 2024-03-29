(ns pubs.comps.app
  (:require
    [pubs.comps.breadcrumbs :as breadcrumbs]
    [pubs.comps.overlay :as overlay]
    [pubs.comps.panels.authors :as authors]
    [pubs.comps.panels.authors-new :as authors-new] 
    [pubs.comps.panels.citations-doi :as citations-doi]
    [pubs.comps.panels.citations-manual :as citations-manual]
    [pubs.comps.panels.databases :as databases]
    [pubs.comps.panels.errors :as errors]
    [pubs.comps.panels.files :as files]
    [pubs.comps.panels.licenses :as licenses]
    [pubs.comps.panels.series :as series]
    [pubs.comps.main :as main]
    [pubs.comps.summary :as summary]
    )
  )

(defn wrap [s]
  [:div.wrap
   (breadcrumbs/render s)
   (if (get-in s [:ui :summary])
     (summary/render s) 
     (main/render s)
     ) 
   ])

(defn click [s e]
  (re-frame.core/dispatch [:tags/close])
  (re-frame.core/dispatch [:options/close])
  )

(defn render [s]
  (merge
    [:div {:on-click #(click s %)}]
    (wrap s)
    (overlay/render s)
    (errors/render s :errors)
    (files/render s :content)
    (files/render s :images)
    (files/render s :support-docs)
    (authors/render s :authors-list)
    (authors-new/render s :authors-new)
    (licenses/render s :licenses)
    (citations-doi/render s :citations-doi)
    (citations-manual/render s :citations-manual)
    (databases/render s :databases)
    (series/render s :series)
;    (help/help s :help-center)
    )
  )

