(use '[clojure.string :only (join split trim)])
(require '[clojure.repl :refer :all])

(def ROWS 3)
(def COLS ROWS)

(defn initial-state
  "Build an initial tic-tac-toe board"
  ([] (initial-state ROWS COLS))
  ([rows cols]
   (let [game  (vec (repeat rows (vec (repeat cols :empty))))]
     (for [[i row] (map-indexed list game)
           [j cell] (map-indexed list row)]
       {:row i :col j :value cell}))))

(defn get-value [cell]
  (get cell :value :empty))

(defn row [state row-index]
  (filter #(= row-index (:row %)) state))

(defn col [state col-index]
  (filter #(= col-index (:col %)) state))

(defn diag [state triangular?]
  (if triangular?
    (filter #(= (:col %)
                (:row %))
            state)
    ; Diagonal from top right to bottom left.
    (filter #(= (- ROWS 1)
                (+ (:col %)
                   (:row %)))
            state)))

(defn winner [state]
  (let [winnable-trios (concat (map #(row state %) (range ROWS))
                               (map #(col state %) (range COLS))
                               (list (diag state true))
                               (list (diag state false)))
        values (map #(map get-value %) winnable-trios)
        winners (filter #(and (apply = %)
                              (not-any? (fn [x] (= x :empty)) %))
                        values)]
    (if (= 0 (count winners))
      nil
      (first (first winners)))))

(defn display-turn [turn]
  (cond (= turn :x) "X"
        (= turn :o) "O"
        :else " "))

(defn cell-string [cell]
  (let [value (:value cell)]
    (display-turn value)))

(defn print-board [state]
  (let [sorted-cells (sort-by #(vec (list (:row %)
                                          (:col %)))
                              state)
        rows (vals (group-by :row sorted-cells))]
    (doseq [row rows]
      (println (join "|" (map cell-string row))))))

(defn move-error [msg]
  (str "Invalid move: " msg))

(defn out-of-bounds? [row col]
  (and (< row 0)
       (>= row ROWS)
       (< col 0)
       (>= col COLS)))

(defn ask-move
  "Ask the player for a move through stdin. Must be of form - row col"
  ([turn]
   (ask-move turn ""))
  ([turn message]
   (println message)
   (print "Please enter a move for " (display-turn turn) ": ")
   (flush)
   (let [move (trim (read-line))
         sections (split move #" ")]
     (if (not= 2 (count sections))
       (recur turn (move-error "Must be of the form: {row} {col}"))
       (try
         (let [[row col] (map #(Integer/parseInt %) sections)]
           (if (out-of-bounds? row col)
             (ask-move turn (move-error "Out of bounds."))
             (list row col)))
         (catch Exception e
           (println (.getMessage e))
           (ask-move turn (move-error "Couldn't convert to integers"))))))))

(defn get-value-at-position [state row col]
  (:value (first (filter #(and (= (:row %) row)
                               (= (:col %) col))
                         state))))

(defn valid-move? [state move]
  (let [[row col] move
        value (get-value-at-position state row col)]
    (= :empty value)))

(defn apply-move [state move turn]
  (let [[row col] move
        state-without-cell (filter #(or (not= (:row %) row)
                                        (not= (:col %) col))
                                   state)
        cell {:row row :col col :value turn}]

    (conj state-without-cell cell)))

(defn next-turn [turn]
  (if (= turn :x) :o :x))

(defn tic-tac-toe
  ([]
   (tic-tac-toe (initial-state) :x))
  ([state] (tic-tac-toe state :x))
  ([state turn]
   (print-board state)
   (let [winning-player (winner state)]
     (if winning-player
       (println (display-turn winning-player) "won!")
       (let [move (ask-move turn)]
         (if (valid-move? state move)
           (recur (apply-move state move turn) (next-turn turn))
           (recur state turn)))))))

(tic-tac-toe)
