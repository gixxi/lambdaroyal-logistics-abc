(ns lambdaroyal.logistics.abc.t-util
  (:use midje.sweet)
  (:require [lambdaroyal.logistics.abc.util :as util]))

(def in1 {:a 1 :c 5 :b 0 :d 2})
(def out1 (repeatedly 1000 #(util/select-randomly in1)))

(defn ratio [coll filtered expected delta]
  (let [_ (println :counts (map count [coll filtered]))
        abs (/ (count filtered) (count coll))
        _ (println :ratio abs)
        abs (- abs expected)]
    (<= abs delta)))
   

(fact (some #(= % :a) out1) => true)
(fact (some #(= % :c) out1) => true)
(fact (some #(= % :d) out1) => true)
(fact (some #(= % :b) out1) => nil?)

(fact (ratio out1 (filter #(= % :a) out1) 1/8 0.1) => true)
(fact (ratio out1 (filter #(= % :c) out1) 5/8 0.1) => true)
(fact (ratio out1 (filter #(= % :d) out1) 2/8 0.1) => true)

(fact (util/select-randomly {}) => nil)
