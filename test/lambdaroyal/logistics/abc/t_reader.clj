(ns lambdaroyal.logistics.abc.t-reader
  (:use midje.sweet)
  (:require [lambdaroyal.logistics.abc.reader :as reader])
  (:require [lambdaroyal.logistics.abc.cluster :as cluster]))

;;loading the file
(def f (clojure.java.io/as-file "Bat_Test.rpt"))
(def in1 (reader/load-csv f))
(fact (count in1) => 28)

;;reading the adjacence matrix
(def in2 (reader/process in1))
(fact (-> in2 keys sort) => (-> (map :lwalpos_artid in1) distinct sort))

;;checking graph
(fact ((comp #(get % 14464) #(get % 14465)) in2) => 13.0)
(fact ((comp #(get % 14465) #(get % 14464)) in2) => 174.0)
(fact ((comp #(get % 17268) #(get % 14465)) in2) => 12.0)

;;check coherence-list
(def in3 (reader/coherence-list in2))
(println in3)

;;check amount-per-article
(def in4 (reader/amount-per-article in1))
(println in4)
(fact (get in4 40000) => 600.0)
(fact (get in4 14464) => 174.0)
(fact (get in4 14465) => 19.0)
(fact (apply + (vals in4)) => 1612.0)

;;check random walk
(def from40000 (cluster/random-walk in2 in4 (first in3) 1500 10 #{}))
(println from40000)

;;check clustering
(def clusters (cluster/cluster in2 in4 in3 2 100))
(println :clusters clusters)

(def clusters2 (cluster/cluster f 2 100))
(println :clusters2 clusters2)
