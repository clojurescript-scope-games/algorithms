(ns algorithms.sort.quicksort)

(defn naive-qsort [[pivot & col]]
  (when pivot
    (let [{smaller-part true greater-part false}
          (group-by #(< % pivot) col)]
      (concat (naive-qsort smaller-part)
              [pivot]
              (naive-qsort greater-part)))))

(defn lazy-qsort [[pivot & col]]
  (when pivot
    (let [{smaller-part true greater-part false}
          (group-by #(< % pivot) col)]
      (lazy-cat (lazy-qsort smaller-part)
                [pivot]
                (lazy-qsort greater-part)))))

(defn swap [a i j]
  (let [t (aget a i)]
    (aset a i (aget a j))
    (aset a j t)))

;; Hoare partition scheme
(defn apartition [a pivot i j]
  (loop [i i j j]
    (if (<= i j)
      (let [v (aget a i)]
        (if (< v pivot)
          (recur (inc i) j)
          (do
            (when (< i j)
              (aset a i (aget a j))
              (aset a j v))
            (recur i (dec j)))))
      i)))

(defn qsort
  ([a]
   (qsort a 0 (long (alength a))))
  ([a lo hi]
   (if (< (inc lo) hi)
     (let [pivot (aget a lo)
           split (dec (apartition a pivot (inc lo) (dec hi)))]
       (when (> split lo)
         (swap a lo split))
       (qsort a lo split)
       (qsort a (inc split) hi))
     a)))

(defn lomuto-apartiton [a lo hi]
  (let [pivot (aget a lo)]
    (loop [last lo
           i    (inc lo)]
      (if (<= i hi)
        (if (< (aget a i) pivot)
          (do
            (swap a i (inc last))
            (recur (inc last) (inc i)))
          (recur last (inc i)))
        (do
          (swap a lo last)
          last)))))

(defn lomuto-qsort
  ([a]
   (lomuto-qsort a 0 (long (dec (alength a)))))
  ([a lo hi]
   (if (< lo hi)
     (let [p (lomuto-apartiton a lo hi)]
       (lomuto-qsort a lo (dec p))
       (lomuto-qsort a (inc p) hi))
     a)))

(defn rand-long-array []
  (-> (repeatedly 10000 #(rand-int 100000000))
      (into-array)))

(comment

  (def col (repeatedly 10000 #(rand-int 100000000)))

  (dotimes [_ 10]
    (let [as (rand-long-array)]
      (time
        (qsort as))))
  ;; "Elapsed time: 33.000000 msecs"
  ;; "Elapsed time: 33.000000 msecs"
  ;; "Elapsed time: 31.000000 msecs"
  ;; "Elapsed time: 33.000000 msecs"
  ;; "Elapsed time: 33.000000 msecs"
  ;; "Elapsed time: 32.000000 msecs"
  ;; "Elapsed time: 32.000000 msecs"
  ;; "Elapsed time: 32.000000 msecs"
  ;; "Elapsed time: 33.000000 msecs"
  ;; "Elapsed time: 33.000000 msecs"

  (dotimes [_ 10]
    (let [as (rand-long-array)]
      (time
        (lomuto-qsort as))))
  "Elapsed time: 91.000000 msecs"
  "Elapsed time: 60.000000 msecs"
  "Elapsed time: 60.000000 msecs"
  "Elapsed time: 59.000000 msecs"
  "Elapsed time: 63.000000 msecs"
  "Elapsed time: 62.000000 msecs"
  "Elapsed time: 66.000000 msecs"
  "Elapsed time: 62.000000 msecs"
  "Elapsed time: 71.000000 msecs"
  "Elapsed time: 63.000000 msecs"

  (dotimes [_ 1]
    (time
      (naive-qsort col)))

  ;; "Elapsed time: 7986.000000 msecs"
  )
