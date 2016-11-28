(ns algorithms.sort.bubble
  (:require [goog.string :as gs]
            goog.string.format))

(def state (atom []))

(defn rand-long-array
  ([]
   (rand-long-array 10000 100000000))
  ([n k]
   (-> (repeatedly n #(rand-int 10))
       (into-array))))

(defn swap [a i j]
  (let [t (aget a i)]
    (aset a i (aget a j))
    (aset a j t)))

;; TODO add custom comparator
(defn bubble-sort
  [a]
  (reset! state [])
  (let [l (alength a)]
    (dotimes [i (dec l)]
      (dotimes [j (- (dec l) i)]
        (let [need-swap? (> (aget a j) (aget a (inc j)))]
          (if need-swap?
            (swap! state conj {:col (seq (aclone a))
                               :prev j
                               :next (inc j)
                               :description (gs/format "Since %s > %s, we'll swap them." (aget a j) (aget a (inc j)))})

            (swap! state conj {:col (seq (aclone a))
                               :prev j
                               :next (inc j)
                               :description "Nothing need to do"}))

          (when need-swap?
            (swap a j (inc j)))))))

  (swap! state conj {:col (seq (aclone a))
                     :description "Finished!"})

  a)

(comment
  (def a (rand-long-array 10 10))

  (bubble-sort a))
