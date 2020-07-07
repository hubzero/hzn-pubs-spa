(ns pubs.enzyme
  (:require ["enzyme-adapter-react-16.3" :as enzyme-adapter]
            ["enzyme" :as enzyme]
            [reagent.core :as reagent :refer [as-element]]))

(enzyme/configure
  (clj->js {:adapter (enzyme-adapter.)}))

(defn shallow [hiccup]
  (-> hiccup as-element enzyme/shallow))
