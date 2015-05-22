(ns clanhr.analytics.core
  (:require [ardoq.analytics-clj :as analytics]
            [environ.core :refer [env]]))

(defn- token
  "Access token for segment.io"
  []
  (or (env :clanhr-segment-token) "o8CiVtd14K65kWGl4sHHEdzk5GbN7NCD"))

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

(defn- build-traits
  "Prepares traits with required fields"
  [traits user-id]
  (cond-> traits
    (not (:userId traits)) (assoc :userId user-id)))

(defn identify
  "Identifies a user and sets user traits"
  [user-id traits]
  (let [traits (build-traits traits user-id)]
    (analytics/identify (get-client)
                        user-id
                        traits
                        (build-options user-id "identify" traits))))

(defn track
  "Tracks an event for a given user"
  ([user-id event-name]
   (track user-id event-name {}))
  ([user-id event-name traits]
   (analytics/track (get-client) user-id event-name traits (build-options user-id event-name traits))))
