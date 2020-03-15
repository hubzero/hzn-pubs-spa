(ns hubzero-pubs.comps.ui)

(defn icon [s i]
  [:div {:class "icon"} [:svg [:use {:xlinkHref i}]]]
  )

