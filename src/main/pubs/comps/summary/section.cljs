(ns pubs.comps.summary.section
  (:require [pubs.comps.summary.field :as field]
            [pubs.comps.summary.collection :as collection]
            [pubs.comps.summary.license :as license]
            [pubs.comps.summary.tags :as tags]))

(defn- fieldtype [s k t bold?]
  ((t {:text #(field/render s k bold?)
       :files #(collection/render s k t)
       :databases #(collection/render s k t)
       :authors-list #(collection/render s k t)
       :license #(license/render s (get-in s k))
       :images #(collection/render s k t)
       :tags #(tags/render s)
       :citations #(collection/render s k t)
       :series #(collection/render s k t)})))

(defn render [s fields]
  [:section {:class [:fieldset :no-header]}
   (doall (map
            (fn [[k label t bold?]]
              [:div {:class [:field :field-summary] :key label}
               [:p {:class :label} (str label ":")]
               (fieldtype s k t bold?)])
            fields))])
