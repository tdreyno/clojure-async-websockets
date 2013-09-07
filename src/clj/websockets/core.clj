(ns websockets.core
  (:require [compojure.core :refer [routes]]
            [compojure.route :as route]
            [websockets.system :as system]))

(def app
  (routes
    (route/files "/" {:root "public"})))

(system/init app)