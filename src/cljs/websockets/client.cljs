(ns websockets.client
  (:require-macros [cljs.core.async.macros :as m :refer [go]])
  (:require [cljs.core.async :as async
             :refer [<! >! chan close! put! take! sliding-buffer
                     dropping-buffer timeout]]
            goog.net.WebSocket))

(let [socket (goog.net.WebSocket.)]
  (.open socket (str "ws://localhost:8080/"))
  (.addEventListener socket goog.net.WebSocket.EventType.MESSAGE (fn [e]
    (.log js/console (.-message e))
    (.send socket "sup?")
    (.send socket "yeah."))))
