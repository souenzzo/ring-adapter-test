(ns ring-adapter-test.request.uri-test
  (:require [clojure.test :refer :all]
            [ring-adapter-test.api :as api]))

(comment
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.http-kit/open")
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.ring-jetty-adapter/open")
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.capra/open")
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.ring-jdk-adapter/open"))

(deftest hello-uri
  (is (= "/hello"
        (-> {:uri "/hello"}
          api/capture-request
          :uri))))

(deftest with-query
  (is (= "/hello"
        (-> {:uri          "/hello"
             :query-string "world"}
          api/capture-request
          :uri))
    "it should trim query from URI"))

