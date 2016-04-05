(defproject clanhr/analytics "1.9.0"
  :description "ClanHR specific analytics"
  :url "https://github.com/clanhr/analytics"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies.edn "https://raw.githubusercontent.com/clanhr/dependencies/master/dependencies.edn"

  :dependency-sets [:clojure :common :clanhr]

  :dependencies [[clj-bugsnag "0.2.9"]
                 [clanhr/clj-librato "0.0.5"]
                 [analytics-clj "0.3.0"]]

  :plugins [[clanhr/shared-deps "0.2.6"]])
