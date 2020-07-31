(ns pubs.comps.tag-input
  (:require ;[reagent.core :as r]
            [pubs.comps.ui :as ui]
            ;[pubs.comps.tag-creator :as tag-creator]
            ;[hzn-pubs-spa.utils :as utils]
            )
  )

(defn add-tag [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:tags/add (-> js/document
                                         (.querySelector ".new-tag")
                                         .-value)])
  )

(defn- click [s tag e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:tags/add (:raw_tag tag)])
  )

(defn- result [s tag]
  [:li.result {:key (:id tag)}
   [:a {:href "#"
        :on-click #(click s tag %)
        } (:raw_tag tag)]
   ]
  )

(defn- results [s]
  (merge [:ul.results]
         (doall (map #(result s %) (:tag-results s)))
         ) 
  )

(defn- search [s e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:tags/search (-> e .-target .-value)])
  )

(defn input [s]
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
                      :onChange #(search s %)
                      :value (get-in s [:ui :tag-str]) 
                      }]
     )])

(defn render [s]
  [:div.ptag.creating {:class (if (get-in s [:ui :tag]) :show)}
   [:div.inner
    (input s)    
    [:a.add.icon { :on-click #(add-tag s %) }
     (ui/icon s "#icon-plus")
     [:span.name "Add"]
     ] 
    ]
   (if (and
         (not (empty? (:tag-query s)))
         (not (empty? (:tag-results s)))
         ) 
     [:div.ui.autocomplete (results s) ]
     ) 
   ]
  )


