(ns clanhr.analytics.errors
  (:require [clanhr.analytics.core :as core]))

(defn exception
  "Registers a exception"
  [ex]
  (println ex))

(defn request-exception
  "Registers a exception from a request"
  [request ex]
  (println ex))

(defn error
  "Registers a specific error"
  [error]
  (println error))
