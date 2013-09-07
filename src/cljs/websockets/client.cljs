(ns websockets.client
  (:require-macros [cljs.core.async.macros :as m :refer [go]])
  (:require [cljs.core.async :as async
             :refer [<! >! chan close! put! take! sliding-buffer
                     dropping-buffer timeout]]
            [websockets.core :as core]))

(let [[in out] (core/connect "ws://localhost:8080/")]
  (go
    (loop []
      (let [e (<! in)]
        (put! out "sup?")
        (put! out "yeah?")
        (recur)))))