(for [meth (.getMethods java.awt.Frame)
      :let [name (.getName meth)]
      ;:when (re-find #"close" name)
      ]
  (println name))

(defn create-frame [dimensions]
  (let [frame (java.awt.Frame.)
        [x y] dimensions]
    (.setSize frame (java.awt.Dimension. x y))
    (.setVisible frame true)
    frame))


(defn draw [frame rows cols f]
  (let [gfx (.getGraphics frame)
        clear #(.clearRect gfx 0 0 cols rows)]
    (clear)
    (doseq [x (range cols)
            y (range rows)]
      (let [v (rem (f x y) 256)]
        (.setColor gfx (java.awt.Color. v v v))
        (.fillRect gfx x y 1 1)))))

(def frame (create-frame [500 500]))
(draw frame 500 500 *)
(.dispose frame)
