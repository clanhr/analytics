(ns clanhr.analytics.core
  (:require [ardoq.analytics-clj :as analytics]
            [environ.core :refer [env]]
            [clojure.walk :as walk]))

(def ^:private segment-test-token "IBqHKDwieBPjkZMtqhYbxStKc0KziG4M")

(defn- token
  "Access token for segment.io"
  []
  (or (env :clanhr-segment-token) segment-test-token))

(defn- test-token
  "Access void token for segment.io"
  []
  (or (env :clanhr-segment-void-token) segment-test-token))

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

(defn make-alias
  "Marks two user-ids as the same identity"
  [user1 user2 test?]
  (analytics/make-alias (get-user-client test?) user1 user2))

(defn stringify-keys
  [traits]
  (walk/stringify-keys traits))

(defn track
  "Tracks an event for a given user"
  ([user-id event-name test?]
   (track user-id event-name {} test?))
  ([user-id event-name traits test?]
   (analytics/track (get-user-client test?)
                    user-id
                    event-name
                    (stringify-keys traits)
                    (build-options user-id event-name traits))))
