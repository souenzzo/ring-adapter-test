(ns ring-adapter-test.request.remote-addr-test
  (:require [clojure.test :refer :all]
            [ring-adapter-test.api :as api]))


(deftest remote-addr
  (is (= true
        (-> {}
          api/capture-request
          :remote-addr
          string?))))
