(ns pubs.comps.citations-options
  (:require [pubs.comps.option :as option])
  )

(defn- manual [s e]
  (.preventDefault e)
  (.stopPropagation e)
  ;(data/get-citation-types s)
  (re-frame.core/dispatch [:panels/show :citations-manual true])
  (re-frame.core/dispatch [:options/close])
  )

(defn- doi [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:panels/show :citations-doi true])
  (re-frame.core/dispatch [:options/close])
  )

(defn render [s]
  [:div.citations-options.options-list {:class (if (get-in s [:ui :options :citations]) :open)}
   [:div.inner
    (merge
      [:ul]
      (option/render s "#icon-file-text2" "Enter a DOI" doi)
      (option/render s "#icon-file-text2" "Enter Manually" manual)
      )
    ]
   ]
  )

