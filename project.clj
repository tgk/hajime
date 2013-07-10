(defproject tryclojure "0.1.0-SNAPSHOT"
  :description "A simple web-based Clojure REPL for trying out Clojure without having to install it."
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [noir "1.3.0-beta10"]
                 [commons-lang/commons-lang "2.5"]
                 [clojail "1.0.6"]
                 [de.ubercode.clostache/clostache "1.3.1"]]

  :jvm-opts ["-Djava.security.policy=example.policy""-Xmx80M"]
  :main hajime.server)
