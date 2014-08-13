(ns lambdaroyal.logistics.abc.util
  (:gen-class))

(defn select-randomly [m]
  "Takes a map {k i}, where k elem K and i elem I, i is a non-negative Long into account and selects a k with probability i / sum(I)"
  (if (empty? m) nil
      (let [sum-I (apply + (vals m))
            sorted (sort-by #(-> % second) m)
            first-k (-> sorted first first)
            bounds (reduce
                    (fn [acc [k v]]
                      (cons
                       (list k (- (-> acc first second) v))
                       acc))
                    [[first-k sum-I]]
                    (next sorted))
            rand (.nextInt (java.util.Random.) sum-I)
            _ (comment (println :sum-I sum-I :Rand rand :bounds bounds))]
        (loop [bounds bounds]
          (cond
           (or
            (empty? (rest bounds))
            (< rand (-> bounds rest first second)))
           (-> bounds first first)
           :else
           (recur (next bounds)))))))
 
