(ns curso7-hospital-5.core
  (:require [clojure.test.check.generators :as gen]))

; Gera valores aleatórios
(println (gen/sample gen/boolean 3))                        ; 3 valores aleatórios booleanos
(println (gen/sample gen/int 20))                           ; gera 20 numeros inteiros
(println (gen/sample gen/string))
(println (gen/sample gen/string-alphanumeric))
(println (gen/sample gen/string-alphanumeric 20))           ; gera até 20 strings

(println (gen/sample (gen/vector gen/small-integer 15) 5))            ; retorna 5 vetores de inteiros, com tamanho até 15 dígitos
(println (gen/sample (gen/vector gen/small-integer 1 5) 10))          ; retorna 10 vetores de inteiros, com tamanho de 1 a 5
(println (gen/sample (gen/vector gen/int) 10))              ; retorna 10 vetores de inteiros
