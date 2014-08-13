(ns lambdaroyal.logistics.abc.cluster
  (:require [lambdaroyal.logistics.abc.reader :as reader])
  (:require [lambdaroyal.logistics.abc.util :as util])
  (:gen-class))

(defn random-walk [adjacence-graph amount-per-article article max-cummulated-amount max-steps articles-to-ignore]
  (loop [articles #{article} article article cummulated-amount 0 step 0]
    (cond
     (or
      (>= cummulated-amount max-cummulated-amount)
      (>= step max-steps))
     articles
     ;;not finished yet
     :else
     (if-let [article (util/select-randomly
                    (apply dissoc
                           (get adjacence-graph article)
                           articles-to-ignore))]
       (recur (conj articles article)
              article
              (+ cummulated-amount (get amount-per-article article))
              (inc step))
       articles))))

(defn cluster
  ([adjacence-graph amount-per-article coherence-list max-clusters max-steps]
  (let [overall-amount (apply + (vals amount-per-article))
        amount-per-cluster (/ overall-amount max-clusters)
        result (vec
     (loop [clusters '() articles-to-ignore #{}]
      (let [article (first (remove #(contains? articles-to-ignore %)
                                   coherence-list))]
        (cond
         (or
          (nil? article)
          (>= (count clusters) max-clusters))
         clusters
         :else
         (let [cluster (random-walk adjacence-graph amount-per-article article amount-per-cluster max-steps articles-to-ignore)]
           (recur (cons cluster clusters) (apply conj articles-to-ignore cluster)))))))]
    (map (fn [e]
           (list e (/ (apply + (map #(get amount-per-article %) e)) overall-amount)))
         result)))
  ([file max-clusters max-steps]
     (let [loaded (reader/load-csv file)
           processed (reader/process loaded)
           amount-per-article (reader/amount-per-article loaded)
           coherence-list (reader/coherence-list processed)]
       (cluster processed amount-per-article coherence-list max-clusters max-steps)
  )))
                                









