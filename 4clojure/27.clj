(def palindrome?
  (fn [word]
    (cond
      (< (count word) 2) true
      (not= (first word) (last word)) false
      :else (recur (->> word
                        (drop 1)
                        (drop-last 1))))))



(assert (palindrome? "racecar") "Racecar should be a palindrome")
(assert (palindrome? "r"))
(assert (palindrome? ""))
(assert (not (palindrome? "fs")))
