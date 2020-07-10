(ns pubs.comps.breadcrumbs

  )

(defn master-type [s]
  [:span.restype.indlist[:span.dataset "dataset"]]
  )

(defn render [s]
  [:div
   [:h3.publications.c-header
    [:span [:a {:href (str "/projects/" (get-in s [:data :prj :alias]))} "Projects"]]
    " / "
    [:span [:a {:href (str
                        "/projects/"
                        (get-in s [:data :prj :alias]))

                } (get-in s [:data :prj :title])]]
    " / "
    [:span [:a {:href (str
                        "/projects/"
                        (get-in s [:data :prj :alias])
                        "/publications"
                        )} "Publications"]]
    " / "
    [:span [:a {:href (str
                        "/projects/"
                        (get-in s [:data :prj :alias])
                        "/publications/"       
                        (get-in s [:data :pub-id])
                        )
                } (if (> (count (get-in s [:data :title])) 0) (get-in s [:data :title]) "Untitled")]]
    " » "
    (master-type s)
    ]
   ]
  )

