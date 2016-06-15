(defproject clanhr/analytics "1.11.0"
  :description "ClanHR specific analytics"
  :url "https://github.com/clanhr/analytics"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clanhr/result "0.11.0"]
                 [commons-logging "1.2"]
                 [clj-time "0.11.0"]
                 [commons-logging "1.2"]
                 [clj-bugsnag "0.2.9"]
                 [clanhr/clj-librato "0.0.5"]
                 [analytics-clj "0.3.0"]

                 ;; disable analytics-clj log
                 [org.slf4j/slf4j-nop "1.7.21"]

                 ;; turn on for major analytics logs
                 ;[org.slf4j/jcl-over-slf4j "1.7.5"]
                 ;[ch.qos.logback/logback-classic "1.0.13"]
                 ;[ch.qos.logback/logback-core "1.0.13"]
                 ])
