(ns clanhr.analytics.core
  (:require [ardoq.analytics-clj :as analytics]
            [environ.core :refer [env]]))

(defn- token
  "Access token for segment.io"
  []
  (or (env :clanhr-segment-token) "o8CiVtd14K65kWGl4sHHEdzk5GbN7NCD"))

(def ^:private client (analytics/initialize (token)))
(def ^:private options {})

(defn identify
  "Identifies a user and sets user traits"
  [user-id traits]
  (analytics/identify client user-id traits))

(defn track
  "Tracks an event for a given user"
  ([user-id event-name]
   (track user-id event-name {}))
  ([user-id event-name traits]
   (analytics/track client user-id event-name traits options)))
