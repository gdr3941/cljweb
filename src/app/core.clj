(ns app.core
  (:require [app.routes :as routes]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.refresh :refer [wrap-refresh]])
  (:gen-class))

(defonce server (atom nil))

(defn start! [{:keys [port dev?] :or {port 3000 dev? false}}]
  (let [handler (cond-> routes/app
                  dev? wrap-refresh)] ; auto-reload page on file change
    (reset! server
            (jetty/run-jetty handler {:port port :join? false}))
    (println (str "→ http://localhost:" port))
    @server))

(defn stop! []
  (when-let [s @server]
    (.stop s)
    (reset! server nil)
    (println "stopped")))

(defn -main [& args]
  (let [dev? (some #{"--dev"} args)]
    (start! {:port 3000 :dev? dev?})))

(comment
  ;; In a REPL:
  (start! {:port 3000 :dev? true})
  (stop!)
  )
