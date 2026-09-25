(ns ring-adapter-test.request.headers-test
  (:require [clojure.test :refer :all]
            [ring-adapter-test.api :as api]))

(comment
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.http-kit/open")
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.ring-jetty-adapter/open"))

(deftest headers
  (is (= {"hello" "world"}
        (-> {:headers {"hello" ["world"]}}
          api/capture-request
          :headers
          (select-keys ["hello"])))))

(deftest multiple-headers
  (is (= {"hello" "a,b"}
        (-> {:headers {"hello" ["a" "b"]}}
          api/capture-request
          :headers
          (select-keys ["hello"]))))
  (is (= {"hello" ";,;"}
        (-> {:headers {"hello" [";" ";"]}}
          api/capture-request
          :headers
          (select-keys ["hello"]))))
  (is (= {"hello" ",,,"}
        (-> {:headers {"hello" ["," ","]}}
          api/capture-request
          :headers
          (select-keys ["hello"]))))
  (is (= {"hello" "a,c\\nd"}
        (-> {:headers {"hello" ["a\n" "c\\nd"]}}
          api/capture-request
          :headers
          (select-keys ["hello"])))))


(deftest cookie-multiple-headers
  (is (= {"cookie" "a; b"}
        (-> {:headers {"cookie" ["a" "b"]}}
          api/capture-request
          :headers
          (select-keys ["cookie"]))))
  (is (= {"cookie" ",; ,"}
        (-> {:headers {"cookie" ["," ","]}}
          api/capture-request
          :headers
          (select-keys ["cookie"]))))
  (is (= {"cookie" ";; ;"}
        (-> {:headers {"cookie" [";" ";"]}}
          api/capture-request
          :headers
          (select-keys ["cookie"]))))
  (is (= {"cookie" "a; c\\nd"}
        (-> {:headers {"cookie" ["a\n" "c\\nd"]}}
          api/capture-request
          :headers
          (select-keys ["cookie"])))))
