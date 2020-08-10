(ns pubs.comps.panels.citations-manual
  (:require [pubs.comps.dropdown :as dropdown]
            [pubs.comps.panels.header :as header]
            [pubs.comps.ui :as ui]
            )
  )

(defn- change [s k f e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:citations/text k f (-> e .-target .-value)])
  )

(defn- create [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:citations/manual])
  )

(defn- text [s k f]
  [:div.field.anchor.err {:key (:name f) :class (if (get-in s [:ui :errors (:name f)]) :with-error)}
   [:label {:for :title} (str (:label f) ":")]
   [:input {:type :text
            :value (get-in s [:data k (:name f)])
            :onChange #(change s k f %)
            }]
   (ui/val-error s (:name f))
   ]
  )

(defn- textfield [s k f]
  (merge
    [:div.field {:key (:name f)}]
    [:label {:for :citation} (str (:label f) ":")]
    [:textarea {:name :citation
                :value (get-in s [:data k (:name f)])
                :onChange #(change s k f %)
                }]
    (if (:hint f) [:p.hint (:hint f)])
    )
  )

(defn- field [s k f]
  (((:type f) {:text #(text s k f)
               :textfield #(textfield s k f)
               :dropdown #(dropdown/render s k f)
               }))
  )

(defn- types [s]
  (->> (:citation-types s)
       (map :type_title)
       (into [])
       )
  )

(defn- fieldset [s k]
  [:fieldset.citations-manual
   [:div.selected-item
    (doall (map #(field s k %) [{:name :type
                                 :label "Type"
                                 :type :dropdown
                                 :options (types s)}
                                {:name :title :label "Title" :type :text}
                                {:name :year :label "Year" :type :text}
                                {:name :month
                                 :label "Month"
                                 :type :dropdown
                                 :options (:months s)}
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
    [:a.btn {:href "#"
             :on-click #(create s %)
             }
     "Add citation"]
    ]
   ]  
  )

(defn render [s k]
  [:div.page-panel.as-panel {:class [k (if (get-in s [:ui :panels k]) :open)]}
   [:div.inner
    (header/render s "Add a citation manually")
    (fieldset s k)
    ]
   ]
  )

