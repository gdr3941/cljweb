# clj-htmx-starter

A minimal Clojure web app starter using **Ring + Reitit + Hiccup + HTMX**.

About 200 lines of code, no build step, no JavaScript written by you.
Demonstrates server-rendered pages plus two HTMX patterns: a click-to-update
counter and a debounced live-search box.

## Requirements

- [Clojure CLI](https://clojure.org/guides/install_clojure) (`clj`)
- Java 11+

## Run it

```bash
clj -M:run
# → http://localhost:3000
```

With auto page-refresh on file save:

```bash
clj -M:run --dev
```

## REPL workflow

```bash
clj -M:dev      # nREPL on port 7888
```

Then in your editor, connect to `localhost:7888` and:

```clojure
(require '[app.core :as core])
(core/start! {:port 3000 :dev? true})
;; edit a file, save, reload the browser
(core/stop!)
```

## What's where

```
deps.edn              — dependencies + run aliases
src/app/core.clj      — server entry point + start!/stop!
src/app/routes.clj    — Reitit route table + middleware
src/app/handlers.clj  — request handlers (one fn per route)
src/app/views.clj     — Hiccup HTML (the only "templating")
```

That's the whole thing. Five files.

## Routes

| Method | Path           | What it does                                 |
|--------|----------------|----------------------------------------------|
| GET    | `/`            | Home page                                    |
| GET    | `/about`       | Static "what's where" page                   |
| GET    | `/counter`     | HTMX demo page (counter + live search)       |
| POST   | `/counter/inc` | Returns just the new count as an HTML fragment |
| POST   | `/search`      | Returns a `<ul>` of matches                  |

## How the HTMX bits work

In `src/app/views.clj`, the counter button is just:

```clojure
[:button {:hx-post "/counter/inc"
          :hx-target "#count"
          :hx-swap "innerHTML"}
 "Increment"]
```

That's the whole trick: when clicked, HTMX makes a POST, takes whatever HTML
the server returns, and stuffs it into `#count`. The server returns plain
HTML — no JSON, no API contract.

The search box uses the same pattern with a different trigger:

```clojure
[:input {:hx-post "/search"
         :hx-trigger "keyup changed delay:200ms, search"
         :hx-target "#results"}]
```

## Where to go next

- **Add CSRF**: flip the `:anti-forgery` flag in `routes.clj` and add the token to forms
- **Add a database**: try [next.jdbc](https://github.com/seancorfield/next-jdbc) + SQLite
- **Add real layouts**: split `views.clj` into `layouts/` and `pages/` as it grows
- **Production**: build an uberjar with [tools.build](https://clojure.org/guides/tools_build)
- **Read the HTMX docs**: <https://htmx.org/docs/> — the whole library is small enough to learn in an afternoon

## License

MIT — do whatever.
