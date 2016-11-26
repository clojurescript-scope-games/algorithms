(ns env.index
  (:require [env.dev :as dev]))

;; undo main.js goog preamble hack
(set! js/window.goog js/undefined)

(-> (js/require "figwheel-bridge")
    (.withModules #js {"react" (js/require "react"), "react-native" (js/require "react-native"), "./assets/images/cljs.png" (js/require "../../assets/images/cljs.png")}
)
    (.start "main"))
