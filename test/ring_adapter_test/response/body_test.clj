(ns ring-adapter-test.response.body-test
  (:require [clojure.java.io :as io]
    #_ring.core.protocols
            [clojure.test :refer [deftest is]]
            [ring-adapter-test.api :as api])
  (:import (java.lang AutoCloseable)
           (java.net.http HttpResponse$BodyHandlers)
           (java.nio.file Files Path)
           (java.nio.file.attribute FileAttribute)))

(set! *warn-on-reflection* true)

(defn tempfile
  ^AutoCloseable []
  (let [f (Files/createTempFile "ring-adapter-test" "response-body" (into-array FileAttribute []))]
    (reify
      AutoCloseable
      (close [_] (Files/deleteIfExists f))
      Path
      (toFile [_]
        (.toFile f)))))

(comment
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.http-kit/open")
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.ring-jetty-adapter/open")
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.capra/open")
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.ring-jdk-adapter/open"))

(deftest string-body
  (is (= "string"
        (-> (constantly {:body   "string"
                         :status 200})
          (api/simple-request {} (HttpResponse$BodyHandlers/ofString))
          :body))))


(deftest iseq
  (is (= "iseq"
        (-> (constantly {:body   (seq ["iseq"])
                         :status 200})
          (api/simple-request {} (HttpResponse$BodyHandlers/ofString))
          :body))))

(comment deftest value-bytes
  (is (= "bytes"
        (-> (constantly {:body   (.getBytes "bytes")
                         :status 200})
          (api/simple-request {} (HttpResponse$BodyHandlers/ofString))
          :body))))


(deftest input-stream
  (is (= "input-stream"
        (-> (constantly {:body   (io/input-stream (.getBytes "input-stream"))
                         :status 200})
          (api/simple-request {} (HttpResponse$BodyHandlers/ofString))
          :body))))

(deftest io-file
  (with-open [^Path p (tempfile)]
    (let [f (.toFile p)]
      (spit f "file")
      (is (= "file"
            (-> (constantly {:body   f
                             :status 200})
              (api/simple-request {} (HttpResponse$BodyHandlers/ofString))
              :body))))))

(deftest value-nil
  (is (= ""
        (-> (constantly {:body   nil
                         :status 200})
          (api/simple-request {} (HttpResponse$BodyHandlers/ofString))
          :body))))
