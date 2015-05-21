(ns clanhr.analytics.errors
  (:require [clanhr.analytics.core :as core]))

(defn exception
  "Registers a exception"
  [ex])

(defn request-exception
  "Registers a exception from a request"
  [request ex])

(defn error
  "Registers a specific error"
  [error])
