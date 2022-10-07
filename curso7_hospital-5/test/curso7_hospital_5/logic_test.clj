(ns curso7-hospital-5.logic-test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [curso7-hospital-5.logic :refer :all]
            [schema.core :as s]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]
            [curso7-hospital-5.model :as h.model]
            [schema-generators.generators :as g])
  (:import (clojure.lang ExceptionInfo)))

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

(def nome-aleatorio-gen
  (gen/fmap                                                 ; aplica essa função (clj.string/join) a cada um dos itens gerados
    clojure.string/join                                     ; une o vetor de letras (5 a 10) em uma string
    (gen/vector gen/char-alphanumeric 5 10)))               ; cria vetor de 5 a 10 caracteres alfanuméricos

(defn transforma-vetor-em-fila [vetor]
  (reduce conj h.model/fila-vazia vetor))                   ; conj adiciona na fila-vazia o primeiro elemento do vetor, em seguida reduce com o segundo elemento

(def fila-nao-cheia-gen
  (gen/fmap
    transforma-vetor-em-fila
    (gen/vector nome-aleatorio-gen 0 4)))

; Para teste de propriedade (ex quantidade de pessoas no HOSPITAL continua a mesma após o transfere?)
; Abordagem razoável porém não agradável, pois usamos o tipo da exceptionm
; e o tipo do tipo para fazer um condiciontal para só então verificar a exception que desejamos
; LOG AND RETHROW é muito ruim, mascara o local e não trata, por que capturar uma exception se sabia que não iria tratar?
; porque a linguagem nos forçou a verificar ExceptionInfo
#_(defn transfere-ignorando-erro
    [hospital para]
    (try
      (transfere hospital :espera para)
      (catch ExceptionInfo e
        (cond
          (= :fila-cheia (:type (ex-data e))) hospital      ; caso tipo :fila-cheia retorna hospital
          :else (throw e)))))                               ; caso contrário aconteceu uma exception inesperada

; Abordagem mais interessante, pois evita Log and Rethrow
; mas perde a capacidade de ex-info (ExceltionInfo, passar dados)
; e ainda há o problema de outras pessoas lançarem IllegalStateException em outras partes do código
; Podemos criar exception específicas, porém pode haver um "boom" de exceptions no sistema
; ou criar variações de tipo como realizado no ex-info
(defn transfere-ignorando-erro [hospital para]
  (try
    (transfere hospital :espera para)
    (catch IllegalStateException _
      hospital)))

#_(defspec transfere-tem-que-manter-a-quantidade-de-pessoas 50
    (prop/for-all
      [;espera gen/char-alphanumeric ; caso de erro de schema
       espera (gen/fmap transforma-vetor-em-fila (gen/vector nome-aleatorio-gen 0 50)) ; caso não haja ninguém para ser transferido (fila-vazia no espera) o transfere quebra
       raio-x fila-nao-cheia-gen
       ultrassom fila-nao-cheia-gen
       vai-para (gen/vector (gen/elements [:raio-x :ultrassom]) 0 50)]
      ; Ocorre erro porque as pessoas são transferidas e a fila de espera fica vazia, caso de erro
      ; (println "espera:" (count espera) "vai-para:" (count vai-para) vai-para)
      (let [hospital-inicial {:espera espera, :raio-x raio-x, :ultrassom ultrassom}
            hospital-final (reduce transfere-ignorando-erro hospital-inicial vai-para)]
        ;(println (count (get hospital-final :raio-x)))
        (= (total-de-pacientes hospital-inicial)
           (total-de-pacientes hospital-final)))))

(defspec transfere-tem-que-manter-a-quantidade-de-pessoas 50
  (prop/for-all
    [espera (gen/fmap transforma-vetor-em-fila (gen/vector nome-aleatorio-gen 0 50)) ; caso não haja ninguém para ser transferido (fila-vazia no espera) o transfere quebra
     raio-x fila-nao-cheia-gen
     ultrassom fila-nao-cheia-gen
     vai-para (gen/vector (gen/elements [:raio-x :ultrassom]) 0 50)]
    (let [hospital-inicial {:espera espera, :raio-x raio-x, :ultrassom ultrassom}
          hospital-final (reduce transfere-ignorando-erro hospital-inicial vai-para)]
      (= (total-de-pacientes hospital-inicial)
         (total-de-pacientes hospital-final)))))

; Retorna um Hospital gerado
(def hospital-gerado (g/generate h.model/Hospital))

(defn adiciona-fila-de-espera [[hospital fila]]
  (assoc hospital :espera fila))

; Retorna um gerador de Hospitais
(def hospital-gen
  (gen/fmap
    adiciona-fila-de-espera
    (gen/tuple
      (gen/not-empty (g/generator h.model/Hospital))
      fila-nao-cheia-gen)))

(def chega-em-gen
  "Gerador de chegadas no hospital"
  (gen/tuple (gen/return chega-em)
             (gen/return :espera)
             nome-aleatorio-gen
             (gen/return 1)))                               ; incremento de uma pessoa no hospital

(defn transfere-gen [hospital]
  "Gerador de transferências no hospital"
  (let [departamentos (keys hospital)]
    (gen/tuple (gen/return transfere)
               (gen/elements departamentos)                 ; um dos elementos do hospital
               (gen/elements departamentos)
               (gen/return 0))))                            ; diferanca = 0, mesma quantidade de pacientes no hospital

(defn acao-gen [hospital]
  (gen/one-of [chega-em-gen (transfere-gen hospital)]))

(defn acoes-gen [hospital]
  (gen/not-empty (gen/vector (acao-gen hospital) 1 100)))

(defn executa-uma-acao
  [situacao [funcao param1 param2 diferenca-se-sucesso]]
  (let [hospital (:hospital situacao)
        diferenca-atual (:diferenca situacao)]              ; pois no caso do `transfere` é passada uma tupla com o a fn transfere, o departamento de origem e o departamento de destino
    (try
      (let [hospital-novo (funcao hospital param1 param2)]
        {:hospital  hospital-novo,
         :diferenca (+ diferenca-se-sucesso diferenca-atual)})
      (catch IllegalStateException _
        situacao))))

(defspec simula-um-dia-do-hospital-nao-perde-pessoas 50
  (prop/for-all
    [hospital-inicial hospital-gen]
    (let [acoes (gen/generate (acoes-gen hospital-inicial))
          situacao-inicial {:hospital hospital-inicial, :diferenca 0}
          total-de-pacientes-inicial (total-de-pacientes hospital-inicial)
          situacao-final (reduce executa-uma-acao situacao-inicial acoes)
          total-de-pacientes-final (total-de-pacientes (:hospital situacao-final))]

      ; final >= inicial pois estão chegando pessoas ou nãoÒ
      ; (is (>= total-de-pacientes-final total-de-pacientes-inicial))
      (println "Totais:" total-de-pacientes-final total-de-pacientes-inicial
               "Diferenca:" (:diferenca situacao-final))
      (is (= (- total-de-pacientes-final (:diferenca situacao-final)) total-de-pacientes-inicial)))))
