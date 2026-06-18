(ns ring-adapter-test.request.body-test
  (:require [clojure.test :refer :all]
            [ring-adapter-test.api :as api])
  (:import (java.io InputStream)
           (java.net.http HttpRequest$BodyPublishers)))

(comment
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.http-kit/open")
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.ring-jetty-adapter/open"))

(deftest body
  (is (= true
        (-> {:request-method :post
             :body           (HttpRequest$BodyPublishers/ofString "ok")}
          api/capture-request
          :body
          (->> (instance? InputStream)))))
  (is (= "ok"
        (-> {:request-method :post
             :body           (HttpRequest$BodyPublishers/ofString "ok")}
          (api/capture-request slurp)
          :body))))
