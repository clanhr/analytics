(ns clanhr.analytics.core-test
  (:require [clojure.test :refer :all]
            [clanhr.analytics.core :as core]))

(def user-id "clojure-test")

(deftest smoke-test-identify
  (core/identify user-id {:name "Clojure Test"} true)
  (core/identify user-id {:userId "Clojure Test"} true)
  (core/identify user-id nil true)
  (core/identify user-id {} true))

(deftest smoke-test-track
  (core/track user-id "Clojure unit test" true))
