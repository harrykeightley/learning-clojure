;; https://4clojure.oxal.org/#/problem/26
;; Fib sequence
;; Write a function which returns the first X fibonacci numbers.

(def fibs
  (fn fib [n]
    (loop [result nil]
      (let [length (count result)]
        (cond (= n length) (reverse result)
              (< length 2) (recur (conj result 1))
              :else (recur (conj result (+ (first result)
                                           (second result)))))))))

(comment (do (println (fibs 1))
             (println (fibs 3))
             (println (fibs 8))))
