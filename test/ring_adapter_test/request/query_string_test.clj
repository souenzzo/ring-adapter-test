(ns ring-adapter-test.request.query-string-test
  (:require [clojure.test :refer :all]
            [ring-adapter-test.api :as api]))



(comment
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.http-kit/open")
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.ring-jetty-adapter/open"))

(deftest query-string
  (is (= "hello"
        (-> {:query-string "hello"}
          api/capture-request
          :query-string))))

