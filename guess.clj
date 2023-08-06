(defn game
  "Begins a guessing game"
  ([upper guesses]
   (game upper (rand-int upper) guesses))
  ([upper goal guesses]
   (if (<= guesses 0)
     (println "Sorry :( the number was" goal)
     (do (println "You have" guesses "guesses left.")
         (print "Guess a number between 0 and" upper "->")
         (flush)
         (let [guess (Integer/parseInt (read-line))]
           (if (= guess goal)
             (println "You did it!")
             (do
               (cond (< guess goal) (println "Too small")
                     (> guess goal) (println "Too big"))
               (flush)
               (recur upper goal (- guesses 1)))))))))

(game 10 4)
