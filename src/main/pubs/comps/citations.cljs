(ns hzn-pubs-spa.comps.citations
  (:require
    [hzn-pubs-spa.utils :as utils] 
    [hzn-pubs-spa.data :as data] 
    [hzn-pubs-spa.comps.ui :as ui] 
    [hzn-pubs-spa.comps.panels :as panels] 
    [hzn-pubs-spa.comps.dropdown :as dropdown] 
    )
  )

(defn create-citation [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (when (utils/citations-manual-valid? s)
    (data/create-citation s)
    (panels/close s e)   
    )
  )

(defn add-doi [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s update-in [:data :citations] conj (first (:doi-results @s)))
  (panels/close s e)
  )

(defn- _search [s v]
  (swap! s assoc :doi-query v)
  (if (> (count v) 0) 
    (data/search-citations s)
    (swap! s dissoc :doi-results)
    )
  )

(defn search-doi [s key]
  [:div.field
   [:label {:for :doi} "DOI:"]
   [:input {:type :text :onChange #(_search s (-> % .-target .-value))}]
   ]
  )
(defn- handle-click [s c e]
  (.preventDefault e)
  (.stopPropagation e)
  ;; If it has an id, it's from our DB so just try it to the pub
  (if (:id c)
    (data/add-citation s c) 
    ;; No id? probably from doi.org, will need to be added to our DB
    (do
      (swap! s assoc-in [:data :citations-manual] c)
      (data/create-citation s)
      (panels/close s e)   
      )
    )
  )

(defn citation [s key c]
  [:p.formatted-meta.key {:class (:id c)
                          :key (:id c)
                          :on-click #(handle-click s c %)
                          } (utils/format-citation c)]
  )

(defn list-citations [s key]
  (merge
    [:div.field] 
    (doall (map #(citation s key %)
                (:doi-results @s)
                ))
    )
  )

(defn- _doi [s key]
  [:fieldset.citations-doi
   [:div.selected-item
    (search-doi s key)
    (list-citations s key)
    ]
   [:hr]
   [:div.field.buttons
    ;[:a.btn {:href "#" :on-click #(add-doi s %)} "Add citation"]  
    [:a.btn.secondary {:href "#" :on-click #(panels/close s %)} "Close"]
    ]
   ]
  )

(defn text [s key f]
  [:div.field.anchor.err {:key (:name f) :class (if (get-in @s [:ui :errors (:name f)]) :with-error)}
   [:label {:for :title} (str (:label f) ":")]
   [:input {:type :text
            :value (get-in @s [:data key (:name f)])
            :onChange (fn [e]
                        (.preventDefault e)
                        (.stopPropagation e)
                        (swap! s assoc-in [:data key (:name f)] (-> e .-target .-value))
                        )}]
   (ui/val-error s (:name f))
   ]
  )

(defn textfield [s key f]
  (merge
    [:div.field {:key (:name f)}]
    [:label {:for :citation} (str (:label f) ":")]
    [:textarea {:name :citation
                :value (get-in @s [:data key (:name f)])
                :onChange (fn [e]
                            (.preventDefault e)
                            (.stopPropagation e)
                            (swap! s assoc-in [:data key (:name f)] (-> e .-target .-value))
                            )}]
    (if (:hint f) [:p.hint (:hint f)])
    ) 
  )


(defn field [s key f]
  (((:type f) {:text #(text s key f)
               :textfield #(textfield s key f)
               :dropdown #(dropdown/dropdown s key f)
               }))
  )

(defn- _manual [s key]
  [:fieldset.citations-manual
   [:div.selected-item
    (doall (map #(field s key %) [{:name :citation-type
                                   :label "Type"
                                   :type :dropdown
                                   :options ["Journal" "Article"]}
                                  {:name :title :label "Title" :type :text}
                                  {:name :year :label "Year" :type :text}
                                  {:name :month
                                   :label "Month"
                                   :type :dropdown
                                   :options (:months @s)}
                                  {:name :author :label "Authors" :type :text}
                                  {:name :journal :label "Journal" :type :text}
                                  {:name :book :label "Book title" :type :text}
                                  {:name :volume :label "Volume" :type :text}
                                  {:name :issue :label "Issue/Number" :type :text}
                                  {:name :pages :label "Pages" :type :text}
                                  {:name :eprint :label "E-print" :type :text}
                                  {:name :isbn :label "ISBN/ISSN" :type :text}
                                  {:name :doi :label "DOI" :type :text}
                                  {:name :series :label "Series" :type :text}
                                  {:name :edition :label "Edition" :type :text}
                                  {:name :publisher :label "Publisher" :type :text}
                                  {:name :url :label "URL" :type :text}
                                  {:name :formatted :label "Formatted citation" :type :textfield :hint "IF PROVIDED, FORMATTED CITATION WILL APPEAR AS TYPED. RECOMMENDED FORMAT: APA"}
                                  ] )) 
    ]
   [:hr]
   [:div.field.buttons
    [:a.btn {:href "#" :on-click #(create-citation s %)} "Add citation"]
    ]
   ]
  )

(defn doi [s key]
  [:div.page-panel.as-panel {:class [key (if (get-in @s [:ui :panels key]) :open)]}
   [:div.inner
    (panels/header s "Add a DOI citation")
    (_doi s key)
    ]
   ]
  )

(defn manual [s key]
  [:div.page-panel.as-panel {:class [key (if (get-in @s [:ui :panels key]) :open)]}
   [:div.inner
    (panels/header s "Add a citation manually")
    (_manual s key)
    ]
   ]
  )

