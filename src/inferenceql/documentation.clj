(ns inferenceql.documentation
  (:require [com.stuartsierra.component :as component]
            [inferenceql.documentation.components :as components])
  (:gen-class))

(defn -main
  []
  (component/start components/http-server))
