(ns pubs.comps.summary.tags)

(defn tag [s t]
  [:div {:class :ptag :key t} [:div {:class :inner}
                              [:a {:href (str "/tags/" (:raw_tag t) )
                                   :target :_blank
                                   } (:raw_tag t)] 
                              ]]
  )

(defn render [s]
  [:div {:class [:item :ui :ptags]}
   (doall (map #(tag s %) (vals (get-in s [:data :tags]))))]
  )

