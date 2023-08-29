(require '[clojure.string :as string])

;; what about pawns?
(def move-deltas {:k (for [x (range -1 2) y (range -1 2)] [x y])})

(defn create-game []
  [:r :h :b :q :k :b :h :r
   :p :p :p :p :p :p :p :p
   nil nil nil nil nil nil nil nil
   nil nil nil nil nil nil nil nil
   nil nil nil nil nil nil nil nil
   nil nil nil nil nil nil nil nil
   :P :P :P :P :P :P :P :P
   :R :H :B :Q :K :B :H :R])

(defn index [position]
  (let [[row col] position]
    (+ col (* 8 row))))

(defn get-piece [game position]
  (get game (index position) nil))

(defn get-colour [piece]
  (if (nil? piece)
    nil
    (let [character (last (str piece))
          code (int character)]
      (if (<= (int \A) code (int \B))
        :white
        :black))))

(defn opposite-turn [turn]
  (if (= turn :white) :black :white))

(defn can-apply-move? [game turn move]
  (let [[start, end] move
        [start-piece end-piece] (map #(get-piece game %1) move)
        [start-colour end-colour] (map get-colour [start-piece end-piece])]
    (cond (nil? start-piece) false
          (= start-colour (opposite-turn turn)) false
          (= end-colour turn) false
          ;; Check en-passant?
          ;; Check start piece can make that move
          ;; Check move wouldn't place king in check
          :else true)))

(defn get-next-move [game turn]
  ;; Ask user for move until we get a valid move
  ;; if can do move, return move
  )

(defn apply-move [game move])

(defn stuck? [game turn]
  true)

(defn in-check? [game turn] true)

(defn game-over-result [game turn]
  (cond (and (stuck? game turn) (in-check? game turn)) "Checkmate"
        (stuck? game turn) "Stalemate"
        :else nil))

(defn main []
  (loop [game (create-game)
         turn :white]
    (println game)
    (if-let [result (game-over-result game turn)]
      (println result)
      (let [next-move (get-next-move game turn)]
        (recur (apply-move game next-move) (opposite-turn turn))))))
