(ns breakout.utils
  (:require [cljs.pprint :refer [pprint]]
            [goog.string :as gstring]
            [goog.string.format])
  )

(defn log [msg]
  (.log js/console (pprint msg))
  )

(defn str-float
  "Convert float to str rounded to n decimals (default 1)."
  ([x n] (gstring/format (str "%."n"f") x))
  ([x] (str-float x 1))
  )

(defn get-window-size []
  [(.-innerWidth js/window) (.-innerHeight js/window)])

(defn get-canvas-size []
  (let [canvas (breakout.game/game-canvas :canvas)]
    [(.-width canvas) (.-height canvas)]  
    ))

(defn scale-value [x [x-min x-max] [to-min to-max]]
  "Scale given value thats between x-min and x-max to range to-min to-max.
   TODO bug-ridden"
  (let [portion (/ (.abs js/Math (- x x-min)) (- x-max x-min))] 
    (+ to-min (* portion (- to-max to-min)))))

;;TODO
;; (defn calc-bricks [bricks]
;;   "Return vector of all brick (top-left corner) locations."
;;   (let [level [[true true true true true true]
;;                [true true true nil nil true true true]
;;                [nil nil true true true]]]
;;     (loop [row level x-offset 0]
;;       )
;;     )
;;   )
