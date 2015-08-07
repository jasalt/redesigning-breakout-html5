(ns breakout.game
  (:require
   [clojure.set :as set]
   [monet.canvas :as canvas]
   [reagi.core :as r]

   [breakout.utils :refer [log]]
   [breakout.entities :as entities]
   [breakout.levels :refer [get-level]]
   [breakout.hud]
   ))

;; (def init-state
;;   {:angle 0
;;    :bricks (get-level 3)
;;    :ball {:x 0.5, :y 0.5, :vx 0, :vy 0, :slow false}
;;    :pad 40
;;    }
;;   )

;; (def game {:ball-size 2
;;            :pad-size 20})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Setup game area and entities

(def canvas-dom (.getElementById js/document "game"))
(def game-canvas (canvas/init canvas-dom "2d"))

;; Initial values for game entities
(def pad (atom {:x (/ (.-width (:canvas game-canvas)) 2)
                :y (- (.-height (:canvas game-canvas)) 20)
                :w 50 :h 3
                }))

(def ball (atom {:x (/ (.-width (:canvas game-canvas)) 3)
                 :y (/ (.-height (:canvas game-canvas)) 3)
                 :w 3 :h 3
                 :angle (* Math/PI 1.6)}))

(def pad-entity (entities/pad-entity pad))
(def ball-entity (entities/ball-entity game-canvas ball pad))

;; Brick id's are stored as ::breakout.game/brick descendants

(defn add-brick! [canvas x y]
  (let [entity-key (gensym :brick)
        brick (entities/make-brick-entity canvas
                                          entity-key
                                          {:x x :y y})]
    (derive entity-key ::brick)
    (canvas/add-entity game-canvas entity-key brick)
    ))

(defn build-level [level-number]
  (let [bricks (get-level level-number)]
    (doseq [[pos-x pos-y] bricks]
      (add-brick! game-canvas pos-x pos-y)
      )
    ))

(defn remove-brick! [brick-key]
  (canvas/remove-entity game-canvas brick-key)
  (underive brick-key ::breakout.game/brick)

  ;; Change level 
  (when-not (descendants ::brick)
    (build-level 2) ;; TODO
    ))

(defn get-bricks []
  "Return all active bricks."
  (when-let [brick-keys (descendants ::breakout.game/brick)]
    (map #(vector % ((aget (game-canvas :entities) (str %)) :value))
         brick-keys))
  )

;;;;(add-brick! game-canvas (* 35 pos-x) (* 25 pos-y))
;;(build-level 1)
