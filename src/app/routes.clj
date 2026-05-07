(ns app.routes
  (:require [app.handlers :as h]
            [reitit.ring :as ring]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def routes
  [["/"             {:get h/home}]
   ["/about"        {:get h/about}]
   ["/counter"      {:get h/counter-page}]
   ["/counter/inc"  {:post h/counter-inc}]
   ["/search"       {:post h/search}]])

(def app
  (-> (ring/ring-handler
        (ring/router routes)
        (ring/create-default-handler))
      ;; site-defaults gives us params, cookies, etc.
      ;; CSRF is disabled here to keep the demo simple — turn it on for real apps.
      (wrap-defaults (-> site-defaults
                         (assoc-in [:security :anti-forgery] false)))))
