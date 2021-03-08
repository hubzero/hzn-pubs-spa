(ns pubs.handlers.folders)

(defn !pop [db [_ _]]
  (let [pos (get-in db [:files-m :location])]
    (-> db
        (assoc-in [:files-m :location] (butlast pos))
        )))

(defn click-folder [db [_ fullpath]]
  (-> db
      (update-in [:files-m :location] concat [fullpath])))