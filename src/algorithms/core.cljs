(ns algorithms.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [algorithms.handlers]
            [algorithms.subs]
            [algorithms.sort.bubble :as bubble]))

(set! js/window.React (js/require "react"))
(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def slider (r/adapt-react-class (.-Slider ReactNative)))
(def switch (r/adapt-react-class (.-Switch ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(def init-col [5 3 6 2 4 9 7 1 8 0])

(defn number-bar
  [number button-color]
  [view {:margin-right 5}
   [text
    {:style {:color "white"
             :font-weight "bold"
             :text-align "center"}}
    number]
   [view {:style {:background-color button-color
                  :border-radius 1
                  :width 25
                  :height (* 20 number)}}]])

(defn number-button
  [number button-color]
  [view
   {:style {:background-color button-color
            :padding 10
            :border-radius 1
            :margin-right 5}}
   [text
    {:style {:color "white"
             :text-align "center"
             :font-weight "bold"}}
    number]])

(defn button
  ([name button-color press-fn]
   (button name button-color press-fn {}))
  ([name button-color press-fn style]
   [touchable-highlight
    {:style (merge
             {:background-color button-color
              :flex 1
              :max-width 80
              :min-height 40
              :border-radius 5
              :align-items "center"
              :justify-content "center"}
             style)
     :on-press press-fn}
    [text
     {:style {:color "white"
              :text-align "center"
              :font-weight "bold"}}
     name]]))

(defn app-root []
  (bubble/bubble-sort (into-array init-col))

  (let [current-idx (r/atom 0)
        bar? (r/atom true)
        interval (r/atom nil)
        in-autoplay? (r/atom false)]
    (fn []
      (let [{:keys [col prev next description] :as current-state}
            (nth @bubble/state @current-idx)]
        [view {:style {:flex 1
                       :background-color "#353433"}}
         [view {:style {:flex-direction "row"
                        :justify-content "space-between"
                        :margin-top 20
                        :padding 20}}
          [text {:style {:color "#FFF"
                         :font-size 16
                         :font-weight "bold"}}
           "Bubble Sort"]

          [switch {:value @bar?
                   :on-value-change #(reset! bar? %)}]]

         [view {:style {:flex 1
                        :justify-content "center"
                        :align-items "center"}}

          ;; slider
          [text {:style {:color "rgba(255,255,255,0.4)"}}
           "(drag the slider to see each step.)"]
          [slider {:style {:width 280
                           :height 30
                           :margin-bottom 20}
                   :minimumValue 0
                   :maximumValue (dec (count @bubble/state))
                   :step 1
                   :on-value-change (fn [v]
                                      (reset! current-idx v)
                                      (r/flush))
                   :value @current-idx}]

          ;; numbers
          [view {:flex-direction "row"
                 :justify-content "center"
                 :align-items "center"}
           (if @bar?
             (for [[i number] (map-indexed vector col)]
               (let [background-color (cond
                                        (= i prev)
                                        "#9e9d24"

                                        (= i next)
                                        "#26a69a"

                                        :else
                                        "#666"
                                        )]
                 ^{:key i} [number-bar number background-color]))

             (for [[i number] (map-indexed vector col)]
               (let [background-color (cond
                                        (= i prev)
                                        "#9e9d24"

                                        (= i next)
                                        "#26a69a"

                                        :else
                                        "#666"
                                        )]
                 ^{:key i} [number-button number background-color])))]

          [text
           {:style {:margin-top 20
                    :font-weight "bold"
                    :color "rgba(255,255,255,0.4)"}}
           "(" (inc @current-idx) " / " (count @bubble/state) ")"]

          [view {:style {:margin-top 20
                         :flex-direction "row"}}

           (if @in-autoplay?
             [button "pause" "#666" (fn []
                                   (js/clearInterval @interval)
                                   (reset! interval nil)
                                   (reset! in-autoplay? false))
              {:margin-right 20}]

             [button "Auto Play" "#666" (fn []
                                          (reset! in-autoplay? true)
                                          (reset! interval
                                                  (js/setInterval (fn []
                                                                    (if (< @current-idx (dec (count @bubble/state)))
                                                                      (swap! current-idx inc)
                                                                      ;; stop interval
                                                                      (do
                                                                        (js/clearInterval @interval)
                                                                        (reset! interval nil)
                                                                        (reset! in-autoplay? false))))
                                                                  1000)))
              {:margin-right 20}])

           (when (> @current-idx 0)
             [button "<< prev" "#666" (fn [] (swap! current-idx dec))])

           (when (and (> @current-idx 0) (< @current-idx (dec (count @bubble/state))))
             [view {:style {:width 60}}])

           (when (< @current-idx (dec (count @bubble/state)))
             [button "next >>" "#666" (fn [] (swap! current-idx inc))])]

          [text {:style {:color "#FFF"
                         :margin-top 20
                         :font-size 14}}
           (if description description " ")]]]))))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))
