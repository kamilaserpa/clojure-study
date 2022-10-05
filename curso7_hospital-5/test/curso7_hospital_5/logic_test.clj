(ns curso7-hospital-5.logic-test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [curso7-hospital-5.logic :refer :all]
            [schema.core :as s]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]
            [curso7-hospital-5.model :as h.model]))

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
    [fila (gen/vector gen/string-alphanumeric 0 4)          ;vetor alfanumerico de 0 a 4 pessoas
     pessoa gen/string-alphanumeric]
    ;(println pessoa fila)                                   ; printa apenas 10 coimbinações
    (is (= {:espera (conj fila pessoa)}                     ; deve retornar o mesmo hospital com a pessoa na fila
           (chega-em {:espera fila} :espera pessoa)))))

(def nome-aleatorio
  (gen/fmap                                                 ; aplica essa função (clj.string/join) a cada um dos itens gerados
    clojure.string/join                                     ; une o vetor de letras (5 a 10) em uma string
    (gen/vector gen/char-alphanumeric 5 10)))               ; cria vetor de 5 a 10 caracteres alfanuméricos

(defn transforma-vetor-em-fila [vetor]
  (reduce conj h.model/fila-vazia vetor))                   ; conj adiciona na fila-vazia o primeiro elemento do vetor, em seguida reduce com o segundo elemento

(def fila-nao-cheia-gen
  (gen/fmap
    transforma-vetor-em-fila
    (gen/vector nome-aleatorio 0 4)))

(defspec transfere-tem-que-manter-a-quantidade-de-pessoas 1
  (prop/for-all
    [espera fila-nao-cheia-gen                              ; caso não haja ninguém para ser transferido (fila-vazia no espera) o transfere quebra
     raio-x fila-nao-cheia-gen
     ultrassom fila-nao-cheia-gen
     vai-para (gen/elements [:raio-x :ultrassom])]
    (let [hospital-inicial {:espera espera, :raio-x raio-x, :ultrassom ultrassom}
          hospital-final (transfere hospital-inicial :espera vai-para)]
      (= (total-de-pacientes hospital-inicial)
         (total-de-pacientes hospital-final)))))