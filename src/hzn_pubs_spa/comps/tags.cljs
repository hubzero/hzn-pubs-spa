(ns hzn-pubs-spa.comps.tags
  (:require [reagent.core :as r]
            [hzn-pubs-spa.comps.ui :as ui]
            [hzn-pubs-spa.data :as data]
            [hzn-pubs-spa.utils :as utils]
            )
  )

(defn tag-creator [s]
  [:div.ptag.creator {:href "#"
                      :class (if (get-in @s [:ui :tag]) :hide)
                      :on-click (fn [e]
                                  (.preventDefault e)
                                  (.stopPropagation e)
                                  (swap! s assoc-in [:ui :tag] true)
                                  )
                      }
   [:div.inner
    [:div.add.icon
     (ui/icon s "#icon-plus")
     ]
    "Add new tag"
    ]
   ]
  )

(defn add-tag [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (data/add-tag s (-> js/document
                      (.querySelector ".new-tag")
                      .-value))
  )

(defn- _result-click [s tag e]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s dissoc :tag-query)
  (data/add-tag s (:raw_tag tag))
  (swap! s assoc-in [:ui :tag-str] "")
  )

(defn- _result [s tag]
  [:li.result {:key (:id tag)}
   [:a {:href "#"
        :on-click #(_result-click s tag %)
        } (:raw_tag tag)]
   ]
  )

(defn- _results [s]
  (prn "l;djssaldkfjasldkfjsda")
  (merge [:ul.results]
         (doall (map #(_result s %) (:tag-results @s)))
         ) 
  )

(defn- _search [s v e]
  (.preventDefault e)
  (.stopPropagation e)
  (swap! s assoc-in [:ui :tag-str] v)
  (swap! s assoc :tag-query v)
  (if (and (not (empty? v)) (> (count v) 2))
    (data/search-tags s)
    )
  )

(defn tag-input [s]
  [^{:component-did-mount
     #(-> js/document (.querySelector ".new-tag") (.focus))
     ;:component-did-update
     ;#(.log js/console "example-component-did-update")
     }
   (fn [] 
     [:input.new-tag {
                      :type :text
                      :placeholder "Enter tag"
                      :onKeyUp #(if (= 13 (.-keyCode %)) (add-tag s %))
                      ;:on-change #(swap! s assoc-in [:ui :tag-str] (-> % .-target .-value)) 
                      :onChange #(_search s (-> % .-target .-value) %)
                      :value (get-in @s [:ui :tag-str]) 
                      }]
     )])

(defn tag-creating [s]
  [:div.ptag.creating {:class (if (get-in @s [:ui :tag]) :show)}
   [:div.inner
    (tag-input s)    
    [:a.add.icon { :on-click #(add-tag s %) }
     (ui/icon s "#icon-plus")
     [:span.name "Add"]
     ] 
    ]
   (if (and
         (not (empty? (:tag-query @s)))
         (not (empty? (:tag-results @s)))
         ) 
     [:div.ui.autocomplete (_results s) ]
     ) 
   ]
  )

(defn remove-tag [s t e]
  (.preventDefault e)
  (.stopPropagation e)
  (data/rm-tag s (:id t))
  )

(defn tag
  "s state, t is the tag map - JBG" 
  [s t]
  [:a.ptag {:href (str "/tags/" (:raw_tag t))
            :key (:id t)
            :target :_blank
            }
   [:div.inner (:raw_tag t) 
    [:div.remove.icon {:on-click #(remove-tag s t %)}
     (ui/icon s "#icon-cross")
     ]
    ]
   ]
  )

(defn- _sort [tags]
  (->> tags
       (vals)
       (group-by :raw_tag)
       (into (sorted-map))
       (vals)
       (map first)
       )
  )

(defn tags [s]
  [:div#a-tags.field.err {:class (if (get-in @s [:ui :errors :tags]) :with-error)}
   [:label {:for :tags} "Tags:"]
   [:div.field-wrapper
    (merge
      [:div.item.ui.ptags]
      (map #(tag s %) (_sort (get-in @s [:data :tags])))
      (tag-creator s)
      (tag-creating s)
      )
    ]
    (ui/val-error s :tags)
   ]
  )

