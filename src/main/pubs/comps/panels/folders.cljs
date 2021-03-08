(ns pubs.comps.panels.folders
  (:require
    [pubs.comps.ui :as ui] 
    [pubs.utils :as utils] 
    ) 
  )

(defn spf [path file]
  (str path "/" file)
  )

(defn get-id [s k path file]
  (as-> (get-in s [:data k]) $
    (vals $)
    (group-by :path $)
    ($ (spf path file))
    (first $)
    (:id $)
    (str $)
    (keyword $)
    )
  )

(defn scroll-to-top []
  ;; HACK!!! - JBG
  (as-> js/document $
    (.querySelectorAll $ ".page-panel.open .inner")
    (doall (map #(.scrollTo % 0 0) $)) 
    )
  )

(defn click-folder [fullpath e]
  (.stopPropagation e)                                      ;; stop a href events
  (.preventDefault e)
  (re-frame.core/dispatch [:folders/click fullpath]))

(defn render-m [s path fname]
  [:li {:key fname :on-click #(click-folder (str path "/" fname) %)}
   [:div {:class [:inner :folder]}
    [:div {:class [:selected-indicator (if true :selected)] :on-click #()}
     [:div {:class :icon }
      (ui/icon s "#icon-checkmark")
      [:span {:class :name} "Selected indicator"]]]
    [:header
     [:div {:class :icon} (ui/icon s "#icon-folder-open")
      [:span {:class :name} "Folder"]]
     fname]
    ]])
