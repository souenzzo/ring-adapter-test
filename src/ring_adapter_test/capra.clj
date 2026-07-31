(ns ring-adapter-test.capra
  (:require [capra.server :as server])
  (:import (java.net ServerSocket)))

(defn open
  [ring-handler _opts]
  (let [server-port (with-open [socket (ServerSocket. 0)]
                      (.getLocalPort socket))]
    (-> (server/run-server ring-handler :port server-port)
      (vary-meta assoc :server-port server-port))))
