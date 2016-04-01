(ns clanhr.analytics.metrics-test
  (:require [clojure.test :refer :all]
            [clanhr.analytics.metrics :as metrics]))

(deftest postgres-request
  (metrics/postgres-request "test" "clanhr.analytics.postgres" 10 "select * from users")
  (Thread/sleep 3000))

(deftest http-request
  (metrics/http-request "test" "clanhr.analytics.http" 10 {:uri "/"} {:status 200})
  (Thread/sleep 3000))

(deftest api-request
  (metrics/api-request "test" :directory-api 10 {:url "/"} {:status 200})
  (Thread/sleep 3000))

(deftest new-entity
  (metrics/new-entity "user")
  (Thread/sleep 3000))

(deftest track
  (metrics/track "event" "source" 1)
  (Thread/sleep 3000))

