(ns pubs.comps.rich-editor
  (:require ["react-quill" :as quill :default ReactQuill]))

(defn- change [state k new-value]
  (re-frame.core/dispatch [:text/change k new-value])
  (re-frame.core/dispatch [:req/save-pub]))

(def quill-styling-source
  {:rel "stylesheet"
   :href "//cdn.quilljs.com/1.2.6/quill.snow.css"})

(def modules
  {:toolbar [[{"header" [2 3 false]}]
             ["bold" "italic" "underline"]
             ["link"]
             [{"list" "ordered"} {"list" "bullet"}]]})

(def formats
  ["header" "bold" "italic" "underline" "link" "list" "bullet"])

(defn render [state id title k]
  [:div.field {:id id :class (if (get-in state [:ui :errors k]) :with-error)}
   [:link quill-styling-source]
   [:label {:for :title} title]
   [:> ReactQuill
    {:modules modules
     :formats formats
     :value (get-in state [:data k])
     :onChange #(change state k %)}]])
