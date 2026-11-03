(ns ring-adapter-test.enso
  (:require [s-exp.enso :as enso])
  (:import (java.lang AutoCloseable)))

(defn open
  [ring-handler opts]
  (let [server (enso/run-server ring-handler
                                (assoc opts
                                  :port 8080))]
    (reify AutoCloseable
      (close [_]
        (enso/stop server)))))
