(ns pubs.comps.tag-creator
  (:require ;[reagent.core :as r]
            [pubs.comps.ui :as ui]
            ;[hzn-pubs-spa.data :as data]
            ;[hzn-pubs-spa.utils :as utils]
            )
  )

(defn render [s]
  [:div.ptag.creator {:href "#"
                      :class (if (get-in s [:ui :tag]) :hide)
                      :on-click (fn [e]
                                  (.preventDefault e)
                                  (.stopPropagation e)
                                  (re-frame.core/dispatch [:tags/creator])
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

;(defn add-tag [s e]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (data/add-tag s (-> js/document
;                      (.querySelector ".new-tag")
;                      .-value))
;  )
;
;(defn- _result-click [s tag e]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (data/add-tag s (:raw_tag tag))
;  (swap! s assoc-in [:ui :tag-str] "")
;  (swap! s dissoc :tag-query) 
;  )
;
;(defn- _result [s tag]
;  [:li.result {:key (:id tag)}
;   [:a {:href "#"
;        :on-click #(_result-click s tag %)
;        } (:raw_tag tag)]
;   ]
;  )
;
;(defn- _results [s]
;  (merge [:ul.results]
;         (doall (map #(_result s %) (:tag-results @s)))
;         ) 
;  )
;
;(defn- _search [s v e]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (swap! s assoc-in [:ui :tag-str] v)
;  (when (and (not (empty? v)) (> (count v) 2))
;    (swap! s assoc :tag-query v)
;    (data/search-tags s)
;    )
;  )
;
;(defn tag-input [s]
;  [^{:component-did-mount
;     #(-> js/document (.querySelector ".new-tag") (.focus))
;     ;:component-did-update
;     ;#(.log js/console "example-component-did-update")
;     }
;   (fn [] 
;     [:input.new-tag {
;                      :type :text
;                      :placeholder "Enter tag"
;                      :onKeyUp #(if (= 13 (.-keyCode %)) (add-tag s %))
;                      ;:on-change #(swap! s assoc-in [:ui :tag-str] (-> % .-target .-value)) 
;                      :onChange #(_search s (-> % .-target .-value) %)
;                      :value (get-in @s [:ui :tag-str]) 
;                      }]
;     )])
;
;(defn tag-creating [s]
;  [:div.ptag.creating {:class (if (get-in @s [:ui :tag]) :show)}
;   [:div.inner
;    (tag-input s)    
;    [:a.add.icon { :on-click #(add-tag s %) }
;     (ui/icon s "#icon-plus")
;     [:span.name "Add"]
;     ] 
;    ]
;   (if (and
;         (not (empty? (:tag-query @s)))
;         (not (empty? (:tag-results @s)))
;         ) 
;     [:div.ui.autocomplete (_results s) ]
;     ) 
;   ]
;  )
;
;(defn remove-tag [s t e]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (data/rm-tag s (:id t))
;  )

