(ns clanhr.analytics.metrics-test
  (:require [clojure.test :refer :all]
            [clanhr.analytics.metrics :as metrics]))

(deftest postgres-request
  (metrics/postgres-request "test" "clanhr.analytics" 10 "select * from users")
  (Thread/sleep 3000))

