(ns clanhr.analytics.metrics
  (:require [clj-librato.metrics :as metrics]
            [clanhr.analytics.errors :as errors]
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

(defn- verbose?
  "True if the lib should println own debug stuff"
  []
  (= "true" (env :clanhr-analytics-verbose)))

(defn dequeue!
  [queue]
  (loop []
    (let [current-events @queue]
      (if (or (empty? current-events) (compare-and-set! queue current-events []))
        current-events
        (recur)))))

(defn- send-to-librato!
  "Sends data to librato"
  [current-events]
  (metrics/collate (librato-user)
                   (librato-token)
                   current-events
                   []
                   (options)))

(def ^:private events-processor
  (delay (future
           (while true
             (try
               (do (Thread/sleep 1000)
                   (let [current-events (dequeue! events)
                         n-events (count current-events)]
                     (when (< 0 n-events)
                       (let [result (send-to-librato! current-events)]
                         (when (verbose?)
                           (println "** [analytics] Registered" (count current-events) "events on librato:, status:" (:status result)))))))
               (catch Exception e
                 (errors/exception e)))))))

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
  (not= "false" (env :clanhr-analytics-log-librato)))

(defn- register
  "Registers a metric to librato"
  [env-name source event-name value description]
  (when (log-stdout?)
    (println (str "[" source "] " event-name " " value " - " description)))
  (when (log-librato?)
    (let [current-time (int (/ (tc/to-long (t/now)) 1000.0))]
      (register-event {:name event-name
                       :source source
                       :period default-metric-period
                       :measure_time current-time
                       :value value }))))

(defn postgres-request
  "Tracks a postgres query"
  [env-name source elapsed query]
  (register env-name source (str env-name ".postgresql.ms") elapsed query))

(defn http-request
  "Tracks a http request"
  [env-name source elapsed request response]
  (let [uri (:uri request)
        method (:request-method request)]
    (when-not (= :options method)
      (register env-name source (str env-name ".http.ms") elapsed uri)
      (register env-name source (str env-name ".http.rpm") 1 uri))))

(defn http-request-metric-fn
  "Returns a function that tracks http information. Good to use on ring
  middelwares"
  [handler service-name]
  (fn [request]
    (let [start (. System (nanoTime))
          response (handler request)
          elapsed (/ (double (- (. System (nanoTime)) start)) 1000000.0)]
      (http-request (env :clanhr-env) service-name (int elapsed) request response)
      response)))

(defn api-request
  "Tracks a http request to a ClanHR API endpoint"
  [env-name source elapsed request response]
  (let [uri (:url request)
        method (:request-method request)]
    (when-not (= :options method)
      (register env-name (name source) (str env-name ".internal-api.ms") elapsed
                (str method " " uri " " (:status response))))))

