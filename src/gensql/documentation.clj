(ns gensql.documentation
  (:require [com.stuartsierra.component :as component]
            [gensql.documentation.components :as components])
  (:gen-class))

(defn -main
  []
  (component/start components/http-server))
