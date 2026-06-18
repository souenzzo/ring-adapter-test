(ns ring-adapter-test.request.server-port-test
  (:require [clojure.test :refer :all]
            [ring-adapter-test.api :as api]))


(deftest server-port
  (is (= true
        (-> {}
          api/capture-request
          :server-port
          number?))))
