(ns ring-adapter-test.capra
  (:require [capra.server :as server]))

(defn open
  [ring-handler _opts]
  (server/run-server ring-handler :port 8080))
