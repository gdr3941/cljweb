(ns app.handlers
  (:require [app.views :as v]
            [clojure.string :as str]))

;; --- tiny in-memory state, just to make the demo feel real ---
(defonce state (atom {:count 0}))

(def companies
  ["Anduril" "Cerebras" "Tenstorrent" "Ayar Labs" "Etched"
   "Verkada" "Wayve" "Enovix" "Reliable Robotics" "True Anomaly"
   "Bright Machines" "Peak Energy" "Redwood Materials" "Ursa Major"
   "VulcanForms" "The Nuclear Company"])

;; --- response helpers ---
(defn html-resp [body]
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body body})

;; --- page handlers ---
(defn home [_req]
  (html-resp (v/home)))

(defn about [_req]
  (html-resp (v/about)))

(defn counter-page [_req]
  (html-resp (v/counter-page (:count @state))))

;; --- HTMX fragment handlers ---
(defn counter-inc [_req]
  (let [{:keys [count]} (swap! state update :count inc)]
    (html-resp (v/count-fragment count))))

(defn search [{:keys [params form-params]}]
  (let [q (or (get params "q")
              (get form-params "q")
              "")
        q (str/lower-case (str/trim q))
        matches (if (str/blank? q)
                  []
                  (filter #(str/includes? (str/lower-case %) q) companies))]
    (html-resp (v/search-results matches))))
