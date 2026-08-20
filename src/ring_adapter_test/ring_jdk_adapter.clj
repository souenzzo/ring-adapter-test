(ns ring-adapter-test.ring-jdk-adapter
  (:require
    [ring.adapter.jdk :as jdk])
  (:import (java.lang AutoCloseable)))

(defn open
  [ring-handler _opts]
  (let [server (jdk/server ring-handler {:port 8080})]
    (-> (reify AutoCloseable
          (close [_]
            (jdk/stop server)))
      (vary-meta assoc :server-port 8080))))
