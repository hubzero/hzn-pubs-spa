(ns hzn-pubs-spa.comps.citations
  (:require
    [hzn-pubs-spa.utils :as utils] 
    [hzn-pubs-spa.data :as data] 
    [hzn-pubs-spa.comps.ui :as ui] 
    [hzn-pubs-spa.comps.panels :as panels] 
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

(defn citation [s key c]
  [:p.formatted-meta.key {:class (:id c)
                          :key (:id c)
                          :on-click (fn [e]
                                      (.preventDefault e)
                                      (.stopPropagation e)
                                      (data/add-citation s c)
                                      )
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

(defn- _handle-dropdown [s key f e]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s update-in [:ui key (:name f)] not)
  )

(defn- _option-click [s key f o e]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s assoc-in [:data key (:name f)] o)
  (swap! s assoc-in [:ui key (:name f)] false)
  )

(defn- _option-rm [s key f e]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s assoc-in [:data key (:name f)] "")
  (swap! s assoc-in [:ui key (:name f)] false)
  )

(defn dropdown [s key f]
  [:div.field.anchor.err {:key (:name f) :class (if (get-in @s [:ui :errors (:name f)]) :with-error)}
   [:label {:for (:name f)} (str (:label f) ":")]
   [:div.proto-dropdown {:class (if (get-in @s [:ui key (:name f)]) :open)}
    [:div.input-wrap
     [:input {:type :text
              :value (get-in @s [:data key (:name f)])
              :onChange #(swap! s assoc-in [:data key (:name f)])
              }]
     [:a.icon {:on-click #(_option-rm s key f %)}
      (ui/icon s "#icon-cross")
      ]
     ]
    (merge
      [:ul.dropdown-menu.roll.listbox]    
      (doall (map (fn [o] [:li {:key o
                                :role :option
                                :on-click #(_option-click s key f o %)
                                } o]) (:options f)))
      )
    [:a.icon {:href "#"
              :on-click #(_handle-dropdown s key f %)
              } (ui/icon s "#icon-left")]
    ]
   (ui/val-error s (:name f))
   ]
  )

(defn field [s key f]
  (((:type f) {:text #(text s key f)
               :textfield #(textfield s key f)
               :dropdown #(dropdown s key f)
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
                                  {:name :citation :label "Formatted citation" :type :textfield :hint "IF PROVIDED, FORMATTED CITATION WILL APPEAR AS TYPED. RECOMMENDED FORMAT: APA"}
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

