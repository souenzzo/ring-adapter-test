(ns ring-adapter-test.ring-jetty-adapter
  (:require [ring.adapter.jetty :as jetty])
  (:import (java.lang AutoCloseable)
           (org.eclipse.jetty.server Server)))

(defn open
  [ring-handler opts]
  (let [jetty (jetty/run-jetty ring-handler
                (assoc opts
                  :join? false
                  :port 8080))]
    (reify AutoCloseable
      (close [_]
        (Server/.stop jetty)))))
