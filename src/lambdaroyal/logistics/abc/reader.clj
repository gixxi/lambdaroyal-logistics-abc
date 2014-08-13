(ns lambdaroyal.logistics.abc.reader
  (:require [org.lambdaroyal.util.io :as io])
  (:gen-class))

(defn string-to-int [s]
  (cond (nil? s) nil
        (= "" s) nil
        :else (java.lang.Integer/parseInt (.trim s))))

(defn string-to-float [s]
  (cond (nil? s) nil
        (= "" s) nil
        :else (java.lang.Float/parseFloat (.trim s))))

(defn load-csv [file]
  "Loads all part purchase order line items into a vector.
   Each line of the file needs the structure lwateil_id;lwalpos_id;lwalpos_artid;lwart_nr;lwart_mandid;lwmand_nr;lwalpos_menge"
  (let [key-fun
        [:lwateil_id string-to-int
         :lwalpos_id string-to-int
         :lwalpos_artid string-to-int
         :lwart_nr identity
         :lwart_mandid string-to-int
         :lwmand_nr identity
         :lwalpos_menge string-to-float]]
    (io/lade-datei-und-convert {:file file} :file key-fun
                               #(clojure.string/split % #";")
                               :ignore-first true :encoding "Cp1252")))


(defn process [i]
  "Using the csv content as per load-csv, a adjacence graph A is created, the key is a article_id pointing a map B, where B.key is again a article_id and B.value is an integer coherence factor"
  (let [_ (println "create empty article adjacence graph")
        article-graph (zipmap
                       (map :lwalpos_artid i)
                       (repeat {}))
        _ (println "create order to alpos map")
        order-map (reduce
                   (fn [acc i]
                     (if-let [existing-order (get acc (:lwateil_id i))]
                       (assoc acc (:lwateil_id i)
                              (conj existing-order (select-keys i '(:lwalpos_artid :lwalpos_menge))))
                       (assoc acc (:lwateil_id i)
                              (list
                               (select-keys i '(:lwalpos_artid :lwalpos_menge))))))
                   {} i)
        _ (println "create adjacence list")
        adjacence-list (flatten
                        (map
                         (fn [i]
                           (for [from i
                                 to (distinct (map :lwalpos_artid i))
                                 :when (not= (:lwalpos_artid from) to)]
                             {:from (:lwalpos_artid from)
                              :to to
                              :amount (:lwalpos_menge from)}))
                         (vals order-map)))
        _ (println "build adjacence graph")
        fn-merge (fn [a b] (merge-with (fn [a b] (merge-with + a b))
                                      a b))
        adjacence-graph (reduce fn-merge                               
                                {}
                                (map (fn [i] { (:from i) {(:to i) (:amount i)}})  adjacence-list))]
    adjacence-graph))

(defn coherence-list [adjacence-graph]
  "returns a list of articles sorted descending as per their max coherence to other articles"
  (sort-by
   (fn [n]
     (- (apply max (vals (get adjacence-graph n)))))
   (keys adjacence-graph)))

(defn amount-per-article [i]
  (apply merge-with + (map (fn [n] {(:lwalpos_artid n) (:lwalpos_menge n)}) i)))
