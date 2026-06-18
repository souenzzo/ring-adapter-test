(ns ring-adapter-test.request.uri-test
  (:require [clojure.test :refer :all]
            [ring-adapter-test.api :as api]))

(deftest hello-uri
  (is (= "/hello"
        (-> {:uri "/hello"}
          api/capture-request
          :uri))))
