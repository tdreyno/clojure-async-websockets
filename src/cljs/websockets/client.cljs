(ns websockets.client
  (:require-macros [cljs.core.async.macros :as m :refer [go]])
  (:require [cljs.core.async :as async
             :refer [<! >! chan close! put! take! sliding-buffer
                     dropping-buffer timeout]]
            [websockets.core :as core]))

(defn stringify
  [obj]
  (.stringify js/JSON (clj->js obj)))

(defn hydrate
  [string]
  (js->clj (.parse js/JSON string)))

(let [[in out] (core/connect "ws://localhost:8080/")]
  (go
    (loop []
      (let [e (<! in)
            json (hydrate (.-message e))]
        (.log js/console (get json "label"))
        (put! out (stringify { :label "sup?" }))
        (recur)))))