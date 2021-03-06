(ns clanhr.analytics.core-test
  (:require [clojure.test :refer :all]
            [clanhr.analytics.core :as core]))

(def user-id "clojure-test")

(deftest smoke-test-identify
  (core/identify user-id {:name "Clojure Test"} true)
  (core/identify user-id {:userId "Clojure Test"} true)
  (core/identify user-id nil true)
  (core/identify user-id {} true))

(deftest smoke-test-make-alias
  (core/make-alias "id1" "id2" true))

(deftest smoke-test-track
  (core/track user-id "Clojure unit test" true))

(deftest stringify-keys
  (testing "plain map"
    (is (= {"key-1" 1 "key-2" 2} (core/stringify-keys {:key-1 1 "key-2" 2}))))

  (testing "deeper map"
    (is (= {"key-1" {"key-3" 3} "key-2" 2} (core/stringify-keys {:key-1 {:key-3 3} "key-2" 2}))))
  )
