(ns pubs.comps.rich-editor
  (:require ["react-quill" :as quill :default ReactQuill]))

(defn- change [state k new-value]
  (re-frame.core/dispatch [:text/change k new-value])
  (re-frame.core/dispatch [:req/save-pub]))

(def modules
  {:toolbar [[{"header" [2 3 false]}]
             ["bold" "italic" "underline"]
             ["link"]
             [{"list" "ordered"} {"list" "bullet"}]]
   :clipboard {:matchVisual false}})

(def formats
  ["header" "bold" "italic" "underline" "link" "list" "bullet"])

(defn render [state id title k]
  [:div.field {:id id :class (if (get-in state [:ui :errors k]) :with-error)}
   [:label {:for :title} title]
   [:> ReactQuill
    {:modules modules
     :formats formats
     :value (get-in state [:data k])
     :onChange #(change state k %)}]])
