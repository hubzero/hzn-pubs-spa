(ns pubs.comps.summary.collection
  (:require [pubs.comps.summary.author :as author]
            [pubs.comps.summary.citation :as citation]
            [pubs.comps.summary.database :as database]
            [pubs.comps.summary.file :as file]
            [pubs.comps.summary.image :as image]
            )
  )

(defn render [s k t]
  [:div {:class [:collection :collection-summary]}
   (merge
     [:ul]
     (doall
       (map (fn [i] ((t {:files #(file/render s i)
                         :authors-list #(author/render s i)
                         :images #(image/render s i)
                         :citations #(citation/render s i)
                         :databases #(database/render s i)
                         }))) (as-> (get-in s k) $ (if (map? $) (vals $) $)))
       )
     )
   ]
  )

