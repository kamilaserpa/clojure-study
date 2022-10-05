(ns curso7-hospital-5.logic-test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [curso7-hospital-5.logic :refer :all]
            [schema.core :as s]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]))

(s/set-fn-validation! true)

; Testes escritos baseados em exemplos, que podem ser infinitos
(deftest cabe-na-fila?-test

  (testing "Que cabe numa fila vazia"
    (is (cabe-na-fila? {:espera []} :espera)))

  (testing "Que cabe pessoas em filas de tamanho até 4 inclusive"
    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4))] ; vetor com tamanho entre 0 e 4, gera 10 vetores
      (is (cabe-na-fila? {:espera fila} :espera))))         ; doseq, executa esta linha para cada item do vetor fila, cruza os dados, um por vez

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

#_(deftest chega-em-test
    ; doseq gera uma multiplicação de casos (com valores cruzados), incluindo muitos casos repetidos
    ; nesse caso 10 x 5 = 50 asserts
    (testing "Que é colocada uma pessoa em filas menores que 5"
      (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4) 10)
              pessoa (gen/sample gen/string-alphanumeric 5)]
        (println pessoa fila)
        (is (= 1 1)))))                                     ; para exibir quantidade de casos no resultado do teste


; O teste funciona , mas parece muito uma cópia do nosso código implementado.
; Significa que se eu coloquei um bug no código, provavelmente coloquei aqui tbm, e os testes vão passar
; Não realizamos um teste útil, outra abordagem
(defspec coloca-uma-pessoa-em-filas-menores-que-5 10        ; número de testes gerados = 10
  (prop/for-all
    [fila (gen/vector gen/string-alphanumeric 0 4)
     pessoa gen/string-alphanumeric]
    (println pessoa fila)                                   ; printa apenas 10 coimbinações
    (is (= {:espera (conj fila pessoa)}                     ; deve retornar o mesmo hospital com a pessoa na fila
           (chega-em {:espera fila} :espera pessoa)))))