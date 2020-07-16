(ns hzn-pubs-spa.comps.options
  (:require
    ;[hzn-pubs-spa.utils :as utils]
    ;[hzn-pubs-spa.data :as data]
    ;[hzn-pubs-spa.comps.panels :as panels]
    )
  )


(defn- _remove [s e k id]
  (.preventDefault e)
  (.stopPropagation e)
;  (swap! s assoc-in [:data k] (dissoc (get-in @s [:data k]) id))
  (case k
    :content (re-frame.core/dispatch [:rm-file k id])
    :images (re-frame.core/dispatch [:rm-file k id])
    ;:support-docs (data/rm-file s k id)
    ;:authors-list (data/rm-author s id)
    )
  )
;

;(defn handle-manual [s e]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (data/get-citation-types s)
;  (panels/show-overlay s true)
;  (swap! s assoc-in [:ui :panels :citations-manual] true)
;  (close s)
;  )
;
;(defn- _edit-citation [e s c]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (utils/water-citation s c)
;  (handle-manual s e)
;  )
;
;(defn citation [s k c]
;  (prn "CITATION" c)
;  [:div.options-list.--as-panel {:class (if (get-in @s [:ui :options :citation (:id c)]) :open) }
;   [:div.inner
;    (merge
;      [:ul]
;      (item s "#icon-edit" "Edit" (fn [s e] (_edit-citation e s c)))
;      (item s "#icon-delete" "Remove" (fn [s e]
;                                        (.preventDefault e)
;                                        (.stopPropagation e)
;                                        (prn c)
;                                        (data/rm-citation s (:id c))
;                                        )))
;    ]
;   ]
;  )
;
;



;(defn handle-doi [s e]
;  (.preventDefault e)
;  (.stopPropagation e)
;  (panels/show-overlay s true)
;  (swap! s assoc-in [:ui :panels :citations-doi] true)
;  (close s)
;  )
;
;(defn citations [s]
;  [:div.citations-options.options-list {:class (if (get-in @s [:ui :options :citations]) :open)}
;   [:div.inner
;    (merge
;      [:ul]
;      (item s "#icon-file-text2" "Enter a DOI" handle-doi)
;      (item s "#icon-file-text2" "Enter Manually" handle-manual)
;      )
;    ]
;   ]
;  )
