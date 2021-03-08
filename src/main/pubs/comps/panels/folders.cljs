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

(defn list-all-from
  "Returns a list of full filenames (including path) of all files at or below the
   currently specified directory location.

   files-map - The content of :files key from the :files-m file list state map.
   location  - A string representing the users current location in the file system."
  [files-map location]
  (let [{files :files subdirs :subdirs} (get files-map location)]
    (concat
      (map #(str location "/" %) files)
      (mapcat (partial list-all-from files-map)
              (map #(str location "/" %) subdirs)))))

(defn file-set-diff [db k & [location]]
  (let [location (or location (-> db (get-in [:files-m :location]) last))
        ;; Default to current location if a subpath isn't specified.
        all-at-loc (into #{} (list-all-from (-> db :files-m :files) location))
        all-sel (into #{} (map :path (-> db :data k vals)))]
    (clojure.set/difference all-at-loc all-sel)))

(defn subpath-fully-selected? [db k & [location]]
  (let [location (or location (-> db (get-in [:files-m :location]) last))
        diff (file-set-diff db k location)]
    ; if the diff is empty, we're subpath fully selected.
    (empty? diff)))

(defn click-select-all
  [db k e & [location]]
  (.stopPropagation e)                                      ;; stop a href events
  (.preventDefault e)
  (let [location (or location (-> db (get-in [:files-m :location]) last))
        selected (get-in db [:data k])
        diff     (file-set-diff db k location)]
    ;; at the current "location", get all files.
    ;; if every file in this list is in the selected files list, then DESELECT all
    ;; otherwise, SELECT all files not currently selected.
    (if (empty? diff)
      (do
        ;; Deselect all of the current elements at or below subpath.
        (doseq [{id :id path :path} (vals selected)]
          (when (.startsWith path location)
            (re-frame.core/dispatch [:req/rm-file k id]))))
      (do
        ;; Select the difference
        (doseq [path diff]
          (re-frame.core/dispatch [:req/add-file {:type  k
                                                  :index 0
                                                  :path  path
                                                  :name  (-> path (clojure.string/split #"/") last)
                                                  }]))))))

(defn click-folder [fullpath e]
  (.stopPropagation e)                                      ;; stop a href events
  (.preventDefault e)
  (re-frame.core/dispatch [:folders/click fullpath]))

(defn render-m [s k path fname]
  (let [fullpath (str path "/" fname)
        selected (subpath-fully-selected? s k fullpath)]
    [:li {:key fname :on-click #(click-folder fullpath %)}
     [:div {:class [:inner :folder]}
      [:div {:class [:selected-indicator (if selected :selected)]
             :on-click #(click-select-all s k % fullpath)}
       [:div {:class :icon}
        (ui/icon s "#icon-checkmark")
        [:span {:class :name} "Selected indicator"]]]
      [:header
       [:div {:class :icon} (ui/icon s "#icon-folder-open")
        [:span {:class :name} "Folder"]]
       fname]
      ]]))
