(ns ring-adapter-test.http-kit
  (:require [org.httpkit.server :as server])
  (:import (java.lang AutoCloseable)))

(defn open
  [ring-handler opts]
  (let [server (server/run-server ring-handler
                 (assoc opts
                   :legacy-return-value? false
                   :port 8080))]
    (reify AutoCloseable
      (close [_]
        (server/server-stop! server)))))
