(ns websockets.system
  (:require [com.keminglabs.jetty7-websockets-async.core :refer [configurator]]
            [clojure.core.async :refer [chan go >! <!]]
            [clojure.core.match :refer [match]]
            [ring.adapter.jetty :refer [run-jetty]]
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

(def c (chan))

(register-ws-app! c)

(def server
  (run-jetty app {:configurator (configurator c)
                  :port 8080, :join? false}))