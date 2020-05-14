(ns hzn-pubs-spa.comps.ui)

(defn icon [s i]
  [:div {:class "icon"} [:svg [:use {:xlinkHref i}]]]
  )

(defn val-error [s k]
  (if-let [err (get-in @s [:ui :errors k])]
    [:div.validation-error
     (str "Please check! " (first err) " " (second err) ". ")]
    ) 
  )
 
