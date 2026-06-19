(ns ring-adapter-test.api
  (:require [clojure.string :as string])
  (:import (java.lang AutoCloseable)
           (java.net URI)
           (java.net.http HttpClient HttpClient$Version HttpHeaders HttpRequest HttpResponse$BodyHandlers)
           (java.time Duration)
           (java.util Optional)))

(defn open
  ^AutoCloseable [& argv]
  (let [open-str (System/getProperty "ring-adapter-test.api/open")

        open (some-> open-str
               symbol
               requiring-resolve
               deref)]
    (when-not open
      (throw (ex-info "Can't find ring-adapter-test.api/open property"
               {:proerty open-str})))
    (apply open argv)))

(defn start-stop-ring
  [ring-handler
   {:keys [request-method headers uri query-string body protocol]
    :or   {request-method :get
           uri            "/"
           headers        {}}
    :as   ring-request}
   response-body-handler]
  (with-open [_http-server (open ring-handler
                             {})]
    (let [http-client (HttpClient/newHttpClient)]
      (try
        (let [http-response (.send http-client
                              (proxy [HttpRequest] []
                                (method [] (-> request-method name string/upper-case))
                                (timeout [] (Optional/of (Duration/ofSeconds 1)))
                                (expectContinue [] false)
                                (bodyPublisher [] (if (contains? ring-request :body)
                                                    (Optional/of body)
                                                    (Optional/empty)))
                                (version [] (if (contains? ring-request :protocol)
                                              (Optional/of
                                                (case protocol
                                                  "HTTP/1.1" HttpClient$Version/HTTP_1_1
                                                  "HTTP/2.0" HttpClient$Version/HTTP_2))
                                              (Optional/empty)))
                                (headers [] (HttpHeaders/of headers
                                              (constantly true)))
                                (uri [] (URI. "http" nil "0" 8080 uri query-string nil)))
                              response-body-handler)]
          {:body    (.body http-response)
           :headers (into {}
                      (map (fn [[k vs]]
                             [k (if (next vs)
                                  (vec vs)
                                  (first vs))]))
                      (.map (.headers http-response)))
           :status  (.statusCode http-response)})
        (finally
          (when (instance? AutoCloseable http-client)
            (AutoCloseable/.close http-client)))))))

(defn capture-request
  ([ring-request]
   (capture-request ring-request identity))
  ([ring-request on-body]
   (let [*request (promise)
         {:keys [status]
          :as   ring-response} (start-stop-ring (fn [ring-request]
                                                  (deliver *request (if (contains? ring-request :body)
                                                                      (update ring-request :body on-body)
                                                                      ring-request))
                                                  {:status 202})
                                 ring-request
                                 (HttpResponse$BodyHandlers/discarding))]
     (when-not (== status 204)
       (throw (ex-info "Unexpected return" ring-response)))
     (if (realized? *request)
       @*request
       (throw (ex-info "Unrealized request" {}))))))


(defn simple-request
  ([ring-handler ring-request]
   (simple-request ring-handler ring-request (HttpResponse$BodyHandlers/discarding)))
  ([ring-handler ring-request response-body-handler]
   (start-stop-ring
     ring-handler
     ring-request
     response-body-handler)))
