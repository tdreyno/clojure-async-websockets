(ns websockets.core
  (:require-macros [cljs.core.async.macros :as m :refer [go]])
  (:require [cljs.core.async :as async
             :refer [<! >! chan close! put! take! sliding-buffer
                     dropping-buffer timeout]]
            goog.net.WebSocket))

(defn connect
  ([host] (connect (chan) (chan) host))
  ([in out host]
    (let [socket (goog.net.WebSocket.)]
      (.open socket host)

      (.addEventListener socket goog.net.WebSocket.EventType.MESSAGE (fn [e]
        (go
          (put! in e))))

      (go
        (loop []
          (let [message (<! out)]
            (.send socket message)
            (recur)))))
    [in out]))