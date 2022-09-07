(ns hospital.core
  (:use [clojure pprint]))

; Fila de espera geral
; laboratorio1 - fila do lab1
; laboratorio2 - fila do lab2
; laboratorio3 - fila do lab3

(println "-------------VETOR---------------")
(defn testa-vetor []
  (let [espera [111 222]]
    (println "> vetor:" espera)
    (println "> conj (vetor):" (conj espera 333))           ; conj - adiciona elemento no final do vetor
    (println "> conj (vetor):" (conj espera) 444)
    (println "> peek (vetor):" (peek espera))               ; captura o último elemento
    (println (pop espera))))                                ; pop - remove o último do vetor, tipo pilha, quem entra por último saí por último

(testa-vetor)

(println "-------------LISTA---------------")

(defn testa-lista []
  (let [espera '(111 222)]
    (println "> lista:" espera)
    (println "> conj (lista):" (conj espera 333))           ; conj - adiciona elemento no início da coleção
    (println "> conj (lista):" (conj espera 444))
    (println (pop espera))))                                ; pop - remove o primeiro elemento da coleção
(testa-lista)

(println "-------------CONJUNTO---------------")

(defn testa-conjunto []
  (let [espera #{111 222}]
    (println "> conjunto:" espera)
    (println "> conj (conjunto):" (conj espera 333))        ; conj - adiciona elemento de forma desordenada
    (println "> conj (conjunto):" (conj espera 333))))
;(println (pop espera))                                    ; pop - não funciona, resulta em erro, pois requer implementação de uma interface de pilha

(testa-conjunto)


(println "-------------CUSTOM FILA---------------")

(defn testa-fila []
  (let [espera (conj clojure.lang.PersistentQueue/EMPTY "111" "222")] ; adiciona os elementos "111" e "222" em uma fila vazia
    (println "> fila (seq):" (seq espera))                  ; conseguimos utilizar o println transformando em sequencia
    (println "> conj (fila):" (seq (conj espera "333")))    ; conj - adiciona elemento ao final da fila
    (println "> conj (fila):" (seq (pop espera)))           ; pop - remove elemento do final da fila
    (println "> peek (fila):" (peek espera))                ; peek - captura o primeiro elemento da fila
    (pprint espera)))                                       ; pprint a Pretty Printer for clojure aceita apenas um argumento

(testa-fila)