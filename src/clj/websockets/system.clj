(ns websockets.system
  (:require [com.keminglabs.jetty7-websockets-async.core :refer [configurator]]
            [clojure.core.async :refer [chan go >! <!]]
            [cheshire.core :as json]
            [ring.adapter.jetty :refer [run-jetty]]))

(def connections (atom #{}))

(defn close-connection
  [details]
  (swap! connections disj details))

(defn new-connection
  [details]
  (swap! connections conj details)
  (let [{uri :uri in :in out :out} details]
    (go
      (>! in (json/generate-string {:label "Yo"})) ; welcome event
      (loop []
        (let [msg (<! out)]
          (if msg
            (let [json (json/parse-string msg)]
              (prn (get json "label"))
              (recur))
            (close-connection details)))))))

(defn register-ws-app!
  [conn-chan]
  (go
    (while true
      (let [details (<! conn-chan)]
        (new-connection details)))))

(defn init
  [app]
  (let [c (chan)]
    (register-ws-app! c)

    (run-jetty app {:configurator (configurator c)
                    :port 8080, :join? false})))