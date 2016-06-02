(ns clanhr.analytics.errors-test
  (:require [clojure.test :refer :all]
            [result.core :as result]
            [clanhr.analytics.errors :as errors]))

(deftest exception
  (errors/exception (Exception. "Bugsnag test"))
  (errors/exception (Exception. "Bugsnag test with data")
                    {:data1 "data"
                     :data2 "data"}
                    {:user-id "123"
                     :email "test@clanhr.com"
                     :personal-data {:company "Company"}}))

(deftest error
  (testing "non exception instance"
    (let [result (errors/error {})]
      (is (result/failed? result)))))
