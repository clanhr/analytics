(ns clanhr.analytics.metrics-test
  (:require [clojure.test :refer :all]
            [clanhr.analytics.metrics :as metrics]))

(deftest postgres-request
  (metrics/postgres-request "test" "clanhr.analytics.postgres" 10 "select * from users")
  (Thread/sleep 3000))

(deftest http-request
  (metrics/http-request "test" "clanhr.analytics.http" 10 "/")
  (Thread/sleep 3000))

