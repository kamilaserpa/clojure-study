(ns curso7-hospital-5.schema-generator-study
  (:use clojure.pprint)
  (:require [schema-generators.complete :as c]
            [schema-generators.generators :as g]
            [clojure.test.check.generators :as gen]
            [schema.core :as s]))

(s/def Animal
  {:especie s/Str
   :idade   s/Int})

(s/def Arvore
  {:especie s/Str})

(g/sample 3 Animal)
;; => ({:name "", :barks? false, :type :dog}
;;     {:name "", :claws? false, :type :cat}
;;     {:name "\"|", :claws? false, :type :cat})

(g/generate Arvore)
;; => {:value -8N, :children [{:value 5, :children [{:value -2N, :children []}]}
;;                            {:value -2, :children []}]}

(c/complete {:idade 6} Animal)
;; => {:especie "WgJ$Ssm`71", :idade 6}

(c/complete {:morde true} Animal)
;; => #schema.utils.ErrorContainer{:error {:morde disallowed-key}}

(s/def t (gen/tuple gen/nat gen/boolean))
(gen/sample t)
;; => ([0 false] [0 true] [2 false] [1 true] [0 false] [5 false] [5 false] [0 true] [0 true] [5 true])

(gen/sample (gen/tuple gen/small-integer gen/boolean))
; ([0 true] [0 false] [0 false] [-2 true] [-1 true] [-2 true]
; [5 true] [-2 false] [-1 false] [1 false])

;; generate a vector of booleans, but never the empty vector
(gen/sample (gen/not-empty (gen/vector gen/string-alphanumeric 2)) 4)

