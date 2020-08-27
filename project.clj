(defproject hzn-pubs-spa "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.773"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [thheller/shadow-cljs "2.10.13"]
                 [reagent "0.10.0"]
                 [re-frame "0.12.0"]
                 [metosin/reitit "0.4.2"]
                 [cljs-http "0.1.46"]
                 [day8.re-frame/test "0.1.5"]
                 ]
  :plugins [[lein-shadow "0.2.0"]
            [lein-shell "0.5.0"]]
  :min-lein-version "2.9.0"
  :source-paths ["src/main" "src/test"]
  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "/home/jbg/Development/hub/app/components/com_pubs/site/assets/js/compiled"
                                    "target"]
  :shell {:commands {"open" {:windows ["cmd" "/c" "start"]
                             :macosx  "open"
                             :linux   "xdg-open"}}}
  :shadow-cljs {:nrepl {:port 8777}
                :builds {:app {:target :browser
                               :output-dir "resources/public/js/compiled"
                               :asset-path "/js/compiled"
                               :modules {:app {:init-fn pubs.core/init
                                               :preloads [devtools.preload]}}
                               :devtools {:http-root "resources/public"
                                          :http-port 8280
                                          }
                               }
                         :hub {:target :browser
                               :output-dir "/home/jbg/Development/hub/app/components/com_pubs/site/assets/js/compiled"
                               :asset-path "/app/components/com_pubs/site/assets/js/compiled"
                               :modules {:app {:init-fn pubs.core/init}}
                               :compiler-options {:infer-externs :auto}
                               }
                         :test {:target :node-test
                                :output-to "out/node-tests.js"
                                :autorun   true}
                         }}
  :aliases {"dev"          ["with-profile" "dev" "do"
                            ["shadow" "watch" "app"]]
            "hub"          ["with-profile" "prod" "do"
                            ["shadow" "watch" "hub"]]
            "test"         ["with-profile" "dev" "do"
                            ["shadow" "watch" "test"]]
            "prod"         ["with-profile" "prod" "do"
                            ["shadow" "release" "app"]]
            "build-report" ["with-profile" "prod" "do"
                            ["shadow" "run" "shadow.cljs.build-report" "app" "target/build-report.html"]
                            ["shell" "open" "target/build-report.html"]]
            "karma"        ["with-profile" "prod" "do"
                            ["shadow" "compile" "karma-test"]
                            ["shell" "karma" "start" "--single-run" "--reporters" "junit,dots"]]}
  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "1.0.2"]]
    :source-paths ["dev"]}
   :prod {}
   }
  :prep-tasks [])