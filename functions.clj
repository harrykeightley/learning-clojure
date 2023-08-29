(require '[clojure.repl :refer :all])

(defn greeting
  ([]    "Hello, World!")
  ([x]   (str "Hello, " x "!"))
  ([x y] (str x ", " y "!")))

(greeting)
(greeting "Harry")
(greeting "a" "b")

;; For testing
(assert (= "Hello, World!" (greeting)))
(assert (= "Hello, Clojure!" (greeting "Clojure")))
(assert (= "Good morning, Clojure!"
           (greeting "Good morning" "Clojure")))

(defn do-nothing [x] x)

(do-nothing do-nothing)

(source identity)

(defn always-thing [&] 100)
(always-thing 3)

(defn make-thingy [x]
  (fn [& _] x))

((make-thingy 2))

;; Tests
(let [n (rand-int Integer/MAX_VALUE)
      f (make-thingy n)]
  (assert (= n (f)))
  (assert (= n (f 123)))
  (assert (= n (apply f 123 (range)))))

(source constantly)

(defn triplicate [f]
  (f)
  (f)
  (f))

(triplicate #(println "Hello"))

(defn opposite [f]
  (fn [& args]
    (not (apply f args))))

(defn triplicate2 [f & args]
  (triplicate #(apply f args)))

(triplicate2 println "a" "b")

(assert (= -1.0 (Math/cos Math/PI)))
(Math/cos Math/PI)

(defn square [x]
  (Math/pow x 2))

(square 4)

(defn circling [x]
  (+ (square (Math/sin x))
     (square (Math/cos x))))

(defn approx? [value e]
  #(<= (abs (- value %))
       e))

(def a (map circling (range 10)))

(defn http-get [url]
  (slurp
   (.openStream
    (java.net.URL. url))))

(defn http-get2 [url]
  (slurp url))

(assert (.contains (http-get2 "https://www.w3.org") "html"))

(defn one-less-arg [f x]
  (fn [& args] (apply f x args)))

(defn two-fns [f g]
  (fn [x] (f (g x))))

((two-fns a a) 3)



