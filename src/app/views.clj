(ns app.views
  (:require [hiccup2.core :refer [html]]))

(defn page
  "Wrap a body fragment in the standard HTML shell. Loads HTMX from a CDN."
  [title & body]
  (str
    "<!DOCTYPE html>"
    (html
      [:html {:lang "en"}
       [:head
        [:meta {:charset "utf-8"}]
        [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
        [:title title]
        [:script {:src "https://unpkg.com/htmx.org@2.0.3"
                  :integrity "sha384-0895/pl2MU10Hqc6jd4RvrthNlDiE9U1tWmX7WRESftEDRosgxNsQG/Ze9YMRzHq"
                  :crossorigin "anonymous"}]
        [:style "
          :root { --fg:#222; --muted:#666; --bg:#fafafa; --accent:#3b82f6; }
          * { box-sizing: border-box; }
          body { font-family: ui-sans-serif, system-ui, -apple-system, sans-serif;
                 max-width: 720px; margin: 2rem auto; padding: 0 1rem;
                 color: var(--fg); background: var(--bg); }
          nav a { margin-right: 1rem; color: var(--accent); text-decoration: none; }
          nav a:hover { text-decoration: underline; }
          h1 { margin-bottom: 0.25rem; }
          .muted { color: var(--muted); font-size: 0.9rem; }
          input, button { font: inherit; padding: 0.5rem 0.75rem; border-radius: 6px;
                          border: 1px solid #ccc; }
          button { background: var(--accent); color: white; border: none; cursor: pointer; }
          button:hover { background: #2563eb; }
          .card { background: white; padding: 1rem 1.25rem; border-radius: 8px;
                  border: 1px solid #eee; margin: 1rem 0; }
          .htmx-indicator { display: none; color: var(--muted); margin-left: 0.5rem; }
          .htmx-request .htmx-indicator { display: inline; }
        "]]
       [:body
        [:nav
         [:a {:href "/"} "Home"]
         [:a {:href "/about"} "About"]
         [:a {:href "/counter"} "Counter (HTMX)"]]
        body]])))

(defn home []
  (page "Home"
        [:h1 "Clojure + HTMX Starter"]
        [:p.muted "Ring + Reitit + Hiccup + HTMX — a tiny server-rendered web app."]
        [:div.card
         [:h2 "What this is"]
         [:p "A minimal scaffold to show you how the pieces fit together. "
             "Open the source files and start hacking — this is intentionally small."]]
        [:div.card
         [:h2 "Try the HTMX demo"]
         [:p [:a {:href "/counter"} "→ Counter page"] " — click a button, watch the server update part of the page without a full reload."]]))

(defn about []
  (page "About"
        [:h1 "About"]
        [:p "Five files of Clojure. About 14 KB of HTMX from a CDN. No build step."]
        [:ul
         [:li [:code "deps.edn"] " — dependencies"]
         [:li [:code "src/app/core.clj"] " — server entry point"]
         [:li [:code "src/app/routes.clj"] " — route table"]
         [:li [:code "src/app/handlers.clj"] " — request handlers"]
         [:li [:code "src/app/views.clj"] " — Hiccup HTML"]]))

(defn counter-page [n]
  (page "Counter"
        [:h1 "HTMX Counter"]
        [:p.muted "The button below makes a POST to "
         [:code "/counter/inc"] ". The server returns just the new count. "
         "HTMX swaps it in. No JavaScript written."]
        [:div.card
         [:p "Current count: "
          [:span#count {:style "font-weight:600;font-size:1.5rem;"} n]]
         [:button {:hx-post "/counter/inc"
                   :hx-target "#count"
                   :hx-swap "innerHTML"}
          "Increment"]
         [:span.htmx-indicator "…"]]
        [:div.card
         [:h3 "Live search demo"]
         [:p.muted "Type a name. Each keystroke fires a request after a 200 ms debounce."]
         [:input {:type "search"
                  :name "q"
                  :placeholder "Try: anduril, cerebras, tenstorrent…"
                  :hx-post "/search"
                  :hx-trigger "keyup changed delay:200ms, search"
                  :hx-target "#results"
                  :hx-swap "innerHTML"
                  :autocomplete "off"}]
         [:div#results [:p.muted "(start typing)"]]]))

(defn count-fragment [n]
  ;; Just the inner HTML — HTMX swaps this into #count.
  (str n))

(defn search-results [matches]
  (str
    (html
      (if (empty? matches)
        [:p.muted "no matches"]
        [:ul
         (for [m matches]
           [:li m])]))))
