(defproject clanhr/analytics "1.4.0"
  :description "ClanHR specific analytics"
  :url "https://github.com/clanhr/analytics"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clanhr/result "0.6.0"]
                 [commons-logging "1.1.3"]
                 [clj-time "0.9.0"]
                 [clj-bugsnag "0.2.3"]
                 [environ "1.0.0"]
                 [clj-librato "0.0.5"]
                 [riemann-clojure-client "0.4.1"]
                 [analytics-clj "0.2.2"]])
