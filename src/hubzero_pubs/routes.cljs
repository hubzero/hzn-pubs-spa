(ns hubzero-pubs.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import [goog History]
           [goog.history EventType])
  (:require [secretary.core :as secretary]
            [goog.events :as events]
            [reagent.core :as reagent]            
            [hubzero-pubs.data :as data]
            [hubzero-pubs.comps.panels :as panels]
            [hubzero-pubs.utils :as utils]
            )
  )

;; TODO: Remove, I'm a placeholder! - JBG
(defn set-html! [content]
    (-> (js/document.getElementById "app") 
        (aset "innerHTML" content) 
      )
  )
;; END TODO

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen EventType.NAVIGATE #(secretary/dispatch! (.-token %)))
    (.setEnabled true))
  )

(defn redirect [url]
  (->
    js/window
    .-location
    (set! url)
    )  
  )

(def pubsroot "/pubs/:id/v/:ver-id")

(defn app-routes [s]
  (secretary/set-config! :prefix "#")

  (defroute "/prjs/:id" {:as params}
    (swap! s assoc-in [:ui :summary] false)
    (swap! s assoc-in [:data :prj-id] (:id params))
    ;; Create a new pub - JBG
    (data/save-pub s #(redirect (str "/pubs/#/pubs/" (get-in @s [:data :pub-id])
                                     "/v/" (get-in @s [:data :ver-id])
                                     "/edit")
                                ))
    )

  (defroute (str pubsroot) {:as params}
    (swap! s assoc-in [:ui :summary] true)
    (swap! s assoc-in [:data :pub-id] (:id params))
    (swap! s assoc-in [:data :ver-id] (:ver-id params))

    ;; true is there to validate the pub after it comes back - JBG
    (data/get-pub s true)
    )

  (defroute (str pubsroot "/edit") {:as params}
    ;; This is is a weird hack, dissoc data ... otherwise authors sort breaks - JBG
    (swap! s dissoc :data)
    (swap! s assoc-in [:ui :summary] false)
    (swap! s assoc-in [:data :pub-id] (:id params))
    (swap! s assoc-in [:data :ver-id] (:ver-id params))
    (data/get-pub s)
    )

  (defroute (str pubsroot "/submit") {:as params}
    (if (utils/valid? s)
      (do
        (swap! s assoc-in [:ui :summary] true)
        (data/save-pub s)     
        )
      (panels/show s nil true :errors)
      ) 
    )

  ;; Catch all
  (defroute "*" []
    (set-html! "<h1>LOL! YOU LOST!</h1>"))

  (hook-browser-navigation!))

