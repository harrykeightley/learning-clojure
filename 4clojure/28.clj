(require '[clojure.string :refer [join]])

(def filter-capitals
  (comp (partial join "")
        (partial filter #(and (>= (int %) (int \A))
                              (<= (int %) (int \Z))))))

(filter-capitals "hI")

(comment
  ((filter-capitals "helOO WorlD!")))
