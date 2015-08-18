(defproject lambdaroyal-logistics-abc "0.1-SNAPSHOT"
            :description "LambdaRoyal Logistik Artikel Clusteranalyse"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0" ]
                 [org.apache/poi "3.9"]
                 [org.clojure/data.json "0.2.2"]
                 [org.clojure/tools.cli "0.2.2"]
                 [org.apache.commons/commons-vfs2 "2.0"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [aleph "0.4.0"]
                 [com.keminglabs/c2 "0.1.0"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [org.lambdaroyal/clojure-util "1.0-SNAPSHOT"]
                 [org.lambdaroyal/clojure-algorithms "0.1-SNAPSHOT"]
                 [lein-light-nrepl "0.0.13"]]
  :profiles {:dev 
             {:dependencies [[midje "1.6.3"]]
              :plugins [[lein-midje "3.1.3"]]}}
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]})
