(ns ring-adapter-test.request.protocol-test
  (:require [clojure.test :refer :all]
            [ring-adapter-test.api :as api]))

(comment
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.http-kit/open")
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.ring-jetty-adapter/open"))


(deftest protocol
  (is (= "HTTP/1.1"
        (-> {:protocol "HTTP/1.1"}
          api/capture-request
          :protocol)))
  #_(is (= "HTTP/2.0"
          (-> {:protocol "HTTP/2.0"}
            api/capture-request
            :protocol))))
