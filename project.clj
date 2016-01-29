(defproject clanhr/analytics "1.7.1"
  :description "ClanHR specific analytics"
  :url "https://github.com/clanhr/analytics"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clanhr/result "0.9.3"]
                 [commons-logging "1.2"]
                 [clj-time "0.11.0"]
                 [clj-bugsnag "0.2.3"]
                 [environ "1.0.1"]
                 [clj-librato "0.0.5"]
                 [analytics-clj "0.3.0"]

                 ;; disable analytics-clj log
                 [org.slf4j/slf4j-nop "1.7.12"]

                 ;; turn on for major analytics logs
                 ;[org.slf4j/jcl-over-slf4j "1.7.5"]
                 ;[ch.qos.logback/logback-classic "1.0.13"]
                 ;kkk[ch.qos.logback/logback-core "1.0.13"]
                 ])
