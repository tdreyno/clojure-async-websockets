(ns websockets.core
  (:require [clojure.core.async :refer [go <! >!]]
            [clojure.core.match :refer [match]]
            [compojure.core :refer [routes]]
            [compojure.route :as route]))

(def app
  (routes
   (route/files "/" {:root "public"})))

(defn register-ws-app!
  [conn-chan]
  (go
    (while true
      (match [(<! conn-chan)]
        [{:uri uri :in in :out out}]
        (go
          (>! in "Yo")
          (loop []
            (when-let [msg (<! out)]
              (prn msg)
              (recur))))))))
