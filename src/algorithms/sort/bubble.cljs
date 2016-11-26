(ns algorithms.sort.bubble)

(defn rand-long-array
  ([]
   (rand-long-array 10000))
  ([n]
   (-> (repeatedly n #(rand-int 100000000))
       (into-array))))

(defn swap [a i j]
  (let [t (aget a i)]
    (aset a i (aget a j))
    (aset a j t)))

;; TODO add comparator
(defn bubble-sort
  [a]
  (let [l (alength a)]
    (dotimes [i (dec l)]
      (dotimes [j (- l i)]
        (when (> (aget a j) (aget a (inc j)))
          (swap a j (inc j))))))
  a)

(comment
  (def a (rand-long-array 10))

  (bubble-sort a))
