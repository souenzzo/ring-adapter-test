(ns ring-adapter-test.request.request-method-test
  (:require [clojure.test :refer [deftest are]]
            [ring-adapter-test.api :as api]))

(comment
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.http-kit/open")
  (System/setProperty "ring-adapter-test.api/open" "ring-adapter-test.ring-jetty-adapter/open"))

;; https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Methods
(deftest every-method
  (are [request-method] (= request-method
                          (-> {:request-method request-method}
                            api/capture-request
                            :request-method))
    :get :head :post :delete #_:connect :options :trace :patch
    #__))
