(ns pubs.comps.overlay)

(defn- close [e]
  (.preventDefault e)
  (.stopPropagation e)
  (re-frame.core/dispatch [:panels/close])
  )

(defn render [s]
  [:div {:class :page-overlay :on-click #(close %)}]
  )

