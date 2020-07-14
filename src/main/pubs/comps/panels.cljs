(ns pubs.comps.panels
  (:require
    ;[hzn-pubs-spa.data :as data] 
    [pubs.comps.ui :as ui] 
    ) 
  )

(defn- close [e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:panels/close])
  )

(defn header [s title]
  [:header
   [:a {:href "#" :class :icon :on-click #(close %)}
    (ui/icon s "#icon-left")
    [:span {:class :name} "Return"]
    ]
   [:div {:class :content}
    [:h1 title]
    ]
   ] 
  )

;(defn textfield [s k name]
;  [:div.field.anchor.err {:class (if (get-in @s [:ui :errors (keyword name)]) :with-error)}
;   [:input {:type :text
;            :value (get-in @s [:data k (keyword name)])
;            :on-change #(swap! s assoc-in [:data k (keyword name)]
;                               (-> % .-target .-value)
;                               )}] 
;   (ui/val-error s (keyword name))
;   ]
;  )
;
;(defn field [s key f]
;  [:div {:class :field :key (:name f)}
;   [:label {:for (:name f)} (:label f)]
;   ((:type f) {
;               :text (textfield s key (:name f))
;               })
;   ]
;  )
;
