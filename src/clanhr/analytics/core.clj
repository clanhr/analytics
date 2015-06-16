(ns clanhr.analytics.core
  (:require [ardoq.analytics-clj :as analytics]
            [environ.core :refer [env]]))

(defn- token
  "Access token for segment.io"
  []
  (env :clanhr-segment-token))

(defn- test-token
  "Access void token for segment.io"
  []
  (or (env :clanhr-segment-void-token) "IBqHKDwieBPjkZMtqhYbxStKc0KziG4M"))

(defn- logger
  "Logs events"
  [user-id event-name traits s m]
  (println (str user-id " '" event-name "' " m " : " traits)))

(defn- build-options
  "Builds default options"
  [user-id event-name traits]
  {:callback (partial logger user-id event-name traits)})

(def ^:private client (atom nil))
(defn- get-client
  []
  (swap! client (fn [content]
                  (if content
                    content
                    (analytics/initialize (token))))))

(def ^:private test-client (atom nil))
(defn- get-test-client
  []
  (swap! test-client (fn [content]
                      (if content
                        content
                        (analytics/initialize (test-token))))))

(defn get-user-client
  "Gets the current client to use, based on if it's a test"
  [test?]
  (if test?
    (get-test-client)
    (get-client)))

(defn- build-traits
  "Prepares traits with required fields"
  [traits user-id]
  (cond-> traits
    (not (:language traits)) (assoc :language "en")
    (not (:userId traits)) (assoc :userId user-id)))

(defn identify
  "Identifies a user and sets user traits"
  [user-id traits test?]
  (let [traits (build-traits traits user-id)]
    (analytics/identify (get-user-client test?)
                        user-id
                        traits
                        (build-options user-id "identify" traits))))

(defn track
  "Tracks an event for a given user"
  ([user-id event-name test?]
   (track user-id event-name {} test?))
  ([user-id event-name traits test?]
   (analytics/track (get-user-client test?)
                    user-id
                    event-name
                    traits
                    (build-options user-id event-name traits))))
