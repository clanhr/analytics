(ns clanhr.analytics.errors
  (:require [clanhr.analytics.core :as core]
            [environ.core :refer [env]]
            [result.core :as result]
            [clj-bugsnag.core :as bugsnag]))

(defn- bugsnag-key [] (or (env :bugsnag-key) "a197d052b7b5f50116195db422be4d66"))

(defn exception
  "Registers a exception"
  ([ex] (exception ex {} {}))
  ([ex info] (exception ex info {}))
  ([ex info user]
   (println ex)
   (bugsnag/notify
     ex
     {:api-key (bugsnag-key)
      :environment (or (env :clanhr-env) "test")
      :project-ns "clanhr"
      :meta info
      :user user})
   (result/exception ex)))

(defn request-exception
  "Registers a exception from a request"
  [ex request]
  (println "-- Exception")
  (println request)
  (exception ex request))

(defn error
  "Registers a specific error"
  ([e] (error e {} {}))
  ([e info] (error e info {}))
  ([e info user]
   (exception (RuntimeException. e) info user)
   (result/failure e)))

(Thread/setDefaultUncaughtExceptionHandler
  (reify java.lang.Thread$UncaughtExceptionHandler
    (uncaughtException [_ thread throwable]
      (exception throwable))))
