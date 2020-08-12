(ns pubs.handlers.panels)

;; This is a bit of a hack, but the body is part of the HZ component - JBG
(defn- overlay [show?]
  (as-> js/document $
    (.querySelector $ "body")
    (.-classList $)
    (if show?
      (.add $ "with-overlay")
      (.remove $ "with-overlay")
      )
    )
  )

(defn show [db [_ k show?]]
  (overlay show?)
  (assoc-in db [:ui :panels k] show?) 
  )

(defn close [db [_ _]]
  (overlay false)
  (assoc-in db [:ui :panels] nil)
  )

(defn !pop [db [_ _]]
  (update-in db [:ui :current-panel] pop)
  )

(defn push [db [_ node]]
  (update-in db [:ui :current-panel] conj node)
  )

(defn field [db [_ k n v]]
  (assoc-in db [:data k n] v)
  )

(defn errors [db [_ errors]]
  (re-frame.core/dispatch [:panels/show :errors true])
  (assoc-in db [:ui :errors] errors)
  )

