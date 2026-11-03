(ns ring-adapter-test.enso
  (:require [s-exp.enso :as enso]))

(defn open
  [ring-handler opts]
  (enso/run-server ring-handler
                   (assoc opts
                     :port 8080)))
