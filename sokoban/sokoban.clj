(require '[clojure.java.io :as io])
(require '[clojure.string :as string])

(def DEFAULT_MAP "map1.txt")
(def moves {:w [-1 0] :a [0 -1] :s [1 0] :d [0 1]})

(defrecord Game [tiles size entities player])

(defn parse-tile [tile]
  (cond (= tile \#) :wall
        (= tile \P) :player
        :else :space))

(defn display-tile [tile]
  (cond (= tile :wall) \#
        (= tile :crate) \C
        (= tile :player) \P
        (= tile :goal) \G
        (= tile :filled-goal) \.
        :else \space))

(defn create-map
  ([]
   (create-map DEFAULT_MAP))
  ([map-file]
   (with-open [r (io/reader map-file)]
     (let [raw (reduce conj [] (line-seq r))
           rows (map string/trim raw)
           row-count (count rows)
           col-count (count (first rows))
           result (vec (map parse-tile (string/join rows)))]
       [result [row-count col-count]]))))

; (defn index->position [size index]
;   (let [[rows cols] size
;         row (quot rows index)
;         col (rem rows cols)]
;     [row col]))

(defn position->index [size position]
  (let [[_ cols] size
        [row col] position]
    ;;(println "Pos to index: " size position)
    (+ col (* row cols))))

(defn create-game
  ([]
   (create-game DEFAULT_MAP))
  ([map-file]
   (let [[tiles size] (create-map map-file)]
     (->Game tiles size {[2 2] :crate [5 5] :goal} [1 1]))))

(defn has-won [game]
  (empty? (filter #(= :goal %1) (vals (:entities game)))))

(defn ask-move []
  (print "Please enter a move {w, a, s, d}: ")
  (flush)
  (letfn [(prompt [] (string/trim (read-line)))]
    (loop [move (prompt)]
      (if (contains? moves (keyword move))
        move
        (recur (prompt))))))

(defn in-bounds? [game position]
  (let [[rows cols] (:size game)
        [row col] position]
    (and (< -1 row rows) (< -1 col cols))))

(defn get-tile [game position]
  (let [size (:size game)
        entities (:entities game)
        tiles (:tiles game)
        player (:player game)]
    (cond (not (in-bounds? game position)) :space
          (= player position) :player
          :else (get entities position (tiles (position->index size position))))))

(defn push-crate [game next-position pushed-position]
  (let [pushed-tile (get-tile game pushed-position)
        result-tile (if (= pushed-tile :goal) :filled-goal :crate)
        entities (assoc (:entities game) pushed-position result-tile)
        entities (dissoc entities next-position)]
    (-> game
        (assoc :player next-position)
        (assoc :entities entities))))

(defn apply-move [game move]
  (let [move ((keyword move) moves)
        player (:player game)
        next-position (map + move player)
        pushed-position (map + move next-position) ;; for crates
        next-tile (get-tile game next-position)
        pushed-tile (get-tile game pushed-position)]
    (cond (= next-tile :wall) game
          (#{:filled-goal :space} next-tile) (assoc game :player next-position)
          (and (= next-tile :crate)
               (not (#{:crate :wall} pushed-tile)))
          (push-crate game next-position pushed-position)
          :else game)))

(defn display-game [game]
  (let [[rows cols] (:size game)]
    (doseq [row (range rows)]
      (doseq [col (range cols)]
        (print (display-tile (get-tile game [row col]))))
      (println))))

(defn play-game []
  (loop [game (create-game)]
    (display-game game)
    (if (has-won game)
      (println "Victory")
      (recur (apply-move game (ask-move))))))

(play-game)
