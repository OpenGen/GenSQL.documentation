(ns inferenceql.documentation.components
  (:require [buddy.auth :as auth]
            [buddy.auth.backends :as backends]
            [buddy.auth.middleware :as middleware]
            [crypto.password.bcrypt :as bcrypt]
            [ring.component.jetty :as jetty]
            [ring.middleware.resource :as resource]
            [ring.util.request :as request]
            [ring.util.response :as response]))

;; Encrypt a new password with `bcrypt/encrypt`.
(def encrypted-password "$2a$11$IRz/sOSo.msmHzm50C4PaepQw7w79tDoampsrIljEO3EIAe75g5oy")

(defn not-found-handler
  [_request]
  (response/not-found "Not found"))

(defn authfn
  [_request {:keys [password]}]
  (bcrypt/check password encrypted-password))

(defn wrap-ensure-authenticated
  [handler]
  (fn [request]
    (if (auth/authenticated? request)
      (handler request)
      (auth/throw-unauthorized))))

(defn wrap-redirect-root
  [handler]
  (fn [request]
    (if (= "/" (request/path-info request))
      (response/redirect "/index.html")
      (handler request))))

(def backend (backends/basic {:realm "web" :authfn authfn}))

(def handler
  (-> not-found-handler
      (resource/wrap-resource "site")
      (wrap-redirect-root)
      (wrap-ensure-authenticated)
      (middleware/wrap-authorization backend)
      (middleware/wrap-authentication backend)))

(def app {:handler handler})

(def http-server (jetty/jetty-server {:app app :port 8080}))
