(ns pubs.handlers.folders)

(defn !pop [db [_ _]]
  (update-in db [:ui :current-folder] pop)
  )

(defn push [db [_ folder]]
  (update-in db [:ui :current-folder] conj folder)
  )

