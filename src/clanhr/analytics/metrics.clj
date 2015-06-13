(ns clanhr.analytics.metrics
  (:require [clj-librato.metrics :as metrics]
            [clj-time.core :as t]
            [clj-time.local :as l]
            [clj-time.coerce :as tc]
            [environ.core :refer [env]]))

(defn- librato-user [] (or (env :clanhr-librato-user) "hello@clanhr.com"))
(defn- librato-token [] (or (env :clanhr-librato-token) "01b9c40f133a51729da4cb6399b558732c2c5065af71211918f888fad92fe8ef"))
(def default-metric-period 15)
(def ^:private conn-options (atom nil))
(def ^:private events (atom []))

(defn- options
  "Specific options for request"
  []
  (swap! conn-options (fn [ops]
                        (if ops
                          ops
                          {:connection-manager (metrics/connection-manager {})}))))

(def ^:private events-processor
  (delay (future
    (while true
      (do (Thread/sleep 300)
        (let [current-events @events]
          (when (< 0 (count current-events))
            (metrics/collate (librato-user)
                             (librato-token)
                             current-events
                             []
                             (options))
            (reset! events []))))))))

(defn- register-event
  "Registers an event to be sent later"
  [event]
  (let [touch-runner @events-processor]
    (swap! events conj event)))

(defn- log-stdout?
  "True if the lib should println stuff"
  []
  (= "true" (env :clanhr-analytics-log-stdout)))

(defn- log-librato?
  "True if the lib should send stuff to librato metrics"
  []
  (= "true" (env :clanhr-analytics-log-librato)))

(defn- register
  "Registers a metric to librato"
  [env-name source event-name value description]
  (when (log-stdout?)
    (println (str "[" source "] " event-name " " value " ms - " description)))
  (when (log-librato?)
    (let [current-time (/ (tc/to-long (t/now)) 1000.0)]
      (register-event {:name event-name
                           :source source
                           :period default-metric-period
                           :measure_time current-time
                           :value value }))))

(defn postgres-request
  "Tracks a postgres query"
  [env-name source elapsed query]
  (register env-name source (str env-name ".postgresql") elapsed query))
