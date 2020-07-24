(ns pubs.comps.authors-options
  (:require [pubs.comps.option :as option])
  )

(defn- author [s create? e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:panels/show :authors-new true])
  (re-frame.core/dispatch [:authors/edit create?])
  )

(defn- create [s e]
  (author s true e)
  )

(defn- edit [s v e]
  (re-frame.core/dispatch [:authors/name v])
  (author s false e)
  )

(defn- add [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:req/owners])
  (re-frame.core/dispatch [:panels/show :authors-list true])
  (re-frame.core/dispatch [:options/close])
  )

(defn render [s]
  [:div.authors-options.options-list {:class (if (get-in s [:ui :options :authors]) :open)}
   [:div.inner
    (merge
      [:ul]
      (option/render s "#icon-user" "Add from project" add)
      (option/render s "#icon-user" "Add new" create)
      )
    ]
   ]
  )

