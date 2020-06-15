(ns hzn-pubs-spa.comps.help
  (:require
    [hzn-pubs-spa.comps.ui :as ui] 
    [hzn-pubs-spa.comps.panels :as panels]     
    )
  )

(defn- _header [s]
  [:header
   [:div.content
    [:h1 "Help Center"]
    ]
   [:a.icon {:on-click #(panels/close s %)}
    (ui/icon s "#icon-close")
    ]
   ]
  )

(defn- _container [s]
  [:div.overlay-panel-scroll-container
   [:div.inner
    [:dl
     [:dt "Title"]
     [:dd
      [:p "This title is a title"]
      [:p "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc consequat eget neque eget porta. Morbi fermentum quis nunc eu porttitor. Aliquam non mi aliquet ex ultrices viverra. Quisque ac gravida risus. Vivamus placerat elementum purus, vitae egestas metus sollicitudin eu. Maecenas eget lacinia odio, in dictum arcu. Nunc lobortis dui in mauris congue, quis suscipit est malesuada. Aliquam lobortis diam ut ante consequat, quis lacinia metus ornare. Donec vel sollicitudin massa. Curabitur vehicula varius ante, vitae tristique lacus faucibus sit amet. Phasellus porta, neque nec malesuada luctus, mauris elit pharetra magna, sed ultrices ex erat finibus leo. Etiam eu nunc fermentum, lobortis purus quis, posuere arcu. Aliquam erat volutpat. Nam at ligula vestibulum, dictum nunc vitae, dapibus ex. Donec rutrum leo sed tortor tristique, quis eleifend nisl porttitor."]
      ]
     ]
    ]
   ]  
  )

(defn help [s k]
  [:div.page-panel.as-panel.page-panel-nonmodal.help-center {:class (if (get-in @s [:ui :panels k]) :open)}
   [:div.inner
    (_header s)
    (_container s)
    ]
   ]
  )
