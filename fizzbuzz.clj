(defn get-value [x]
  (cond (and (= 0 (mod x 3))
             (= 0 (mod x 5))) "fizzbuzz"

        (= 0 (mod x 3)) "fizz"
        (= 0 (mod x 5)) "buzz"
        :else (str x)))

(comment (get-value 5))

(defn fizzbuzz
  "Prints the fizzbuzz sequence for the first n numbers."
  [n]
  (doseq [x (range n)]
    (println x (get-value x))))

(comment (fizzbuzz 10))

