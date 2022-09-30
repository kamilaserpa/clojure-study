(ns curso7-hospital-5.logic-test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [curso7-hospital-5.logic :refer :all]
            [schema.core :as s]
            [clojure.test.check.generators :as gen]))

(s/set-fn-validation! true)

; Testes escritos baseados em exemplos, que podem ser infinitos
(deftest cabe-na-fila?-test

  (testing "Que cabe numa fila vazia"
    (is (cabe-na-fila? {:espera []} :espera)))

  (testing "Que cabe pessoas em filas de tamanho até 4 inclusive"
    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4))] ; vetor com tamanho entre 0 e 4, gera 10 vetores
      (is (cabe-na-fila? {:espera fila} :espera))))         ; doseq, executa esta linha para cada item do vetor fila, um por vez

  ; Borda do limite
  (testing "Que não cabe na fila quando a fila está cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5]}, :espera))))

  ; one off da borda do limite pra cima
  (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]}, :espera))))

  ; Dentro das bordas
  (testing "Que cabe na fila quando tem gente mas não está cheia"
    (is (cabe-na-fila? {:espera [1 2 3 4]}, :espera))
    (is (cabe-na-fila? {:espera [1 2]}, :espera)))

  (testing "Que não cabe quando departamento não existe"
    ; hospital possui departamento 'espera' apenas
    (is (not (cabe-na-fila? {:espera [1 2 3 4]}, :raio-x)))))
