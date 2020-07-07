(ns pubs.comps.app
  (:require
   [re-frame.core :as rf]
   [pubs.subs :as subs]
   ))

(defn render []
  (let [name (rf/subscribe [::subs/name])]
    [:div
     [:h1 "Hello from " @name]
     ]))
