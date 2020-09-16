(ns pubs.comps.panels.citations-doi
  (:require [pubs.utils :as utils]
            [pubs.comps.ui :as ui] 
            [pubs.comps.panels.header :as header]
            )
  )

(defn- search [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:citations/search (-> e .-target .-value)])
  )

(defn- close [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:panels/close])
  )

(defn- click [s k c e]
  (.preventDefault e) 
  (.stopPropagation e)
  (re-frame.core/dispatch [:citations/add c])
  )

(defn- citation [s k c]
  [:p.formatted-meta.key {:class (:id c)
                          :key (:id c)
                          :on-click #(click s k c %)
                          :dangerouslySetInnerHTML {:__html (utils/format-citation c)}
                          }
   ]
  )

(defn- results [s k]
  (merge
    [:div.field]
    (doall (map #(citation s k %)
                (:doi-results s)
                ))
    )
  )

(defn search-field [s k]
  [:div.field
   [:label {:for :doi} "DOI:"]
   [:input {:type :text
            :value (:doi-query s)
            :onChange #(search s %)}]
   ]
  )

(defn- fieldset [s k]
  [:fieldset.citations-doi
   [:div.selected-item
    (search-field s k)
    (results s k)
    ]
   [:hr]
   [:div.field.buttons
    [:a.btn.secondary {:href "#" :on-click #(close s %)} "Close"]
    ]
   ]
  )

(defn render [s k]
  [:div.page-panel.as-panel {:class [k (if (get-in s [:ui :panels k]) :open)]}
   [:div.inner
    (header/render s "Add a DOI citation")
    (fieldset s k)
    ]
   ]
  )

