(ns pubs.comps.option)

(defn render [s i name f]
  [:li
   [:a {:href "#" :on-click #(f s %)}
    [:div {:class :icon}
     [:svg [:use {:xlinkHref i}]]
     [:span.name name]
     ] name]
   ]
  )
 
