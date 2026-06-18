(ns ring-adapter-test.request.scheme-test
  (:require [clojure.test :refer :all]
            [ring-adapter-test.api :as api]))

(deftest scheme
  (is (= true
        (-> {}
          api/capture-request
          :scheme
          keyword?))))
