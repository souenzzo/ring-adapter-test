(ns ring-adapter-test.response.headers-test
  (:require [clojure.test :refer [deftest is]]
            [ring-adapter-test.api :as api]))

(deftest hello-header
  (is (= {"hello" "world"}
        (-> (constantly {:headers {"hello" "world"}
                         :status  204})
          (api/simple-request {})
          :headers
          (select-keys ["hello"])))))
