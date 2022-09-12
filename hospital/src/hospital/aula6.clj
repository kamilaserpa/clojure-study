(ns hospital.aula6
  (:use [clojure pprint])
  (:require [hospital.model :as h.model]))

(defn chega-em [fila pessoa]
  (conj fila pessoa))

; Garante que se durante a transação o valor da fila for alterado ocorre o retry
(defn chega-em!
  "Troca de referência= via ref-set"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (ref-set fila (chega-em @fila pessoa))))                ; atribui o valor para fila com o resultado da fn 'chega-em'. '@' extrai o valor do ref

(defn chega-em!
  "Troca de referência via alter"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (alter fila chega-em pessoa)))                          ; alter,

(defn simula-um-dia []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]

    (dosync (chega-em! hospital "guilherme"))               ; dosync explicita uma 'transação' para que o update de um ref possa ser executado
    (pprint hospital)))

; (simula-um-dia)

(println "---------------------------------")

(defn cabe-na-fila? [fila]
  (-> fila
      count
      (< 5)))

(defn chega-em
  [fila pessoa]
  (if (cabe-na-fila? fila)
    (conj fila pessoa)
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa}))))


(defn simula-um-dia []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]

    (dosync (chega-em! hospital "guilherme")
            (chega-em! hospital "maria")
            (chega-em! hospital "lucia")
            (chega-em! hospital "daniela")
            (chega-em! hospital "ana"))
    ;(chega-em! hospital "paulo"))

    (pprint hospital)))

; (simula-um-dia)

(defn async-chega-em! [hospital pessoa]
  (future (Thread/sleep (rand 5000))                        ; aguarda até 5 segundos
          (dosync
            (println "Tentando o codigo sincronizado:" pessoa)
            (chega-em! hospital pessoa))))

(defn simula-um-dia-async []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]
    (def futures (mapv #(async-chega-em! hospital %) (range 10)))
    (future
      (dotimes [n 4]
        (Thread/sleep 2000)
        (pprint hospital)
        (pprint futures)))))


(simula-um-dia-async)
(defn simula-um-dia-async []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]

    ; símbolo global para possibilitar acessar seu valor no REPL
    (def futures (mapv #(async-chega-em! hospital %) (range 10)))

    (future
      (dotimes [n 4]                                        ; executa 4 vzs o que há dento desse bloco de código
        (Thread/sleep 2000)                                 ; aguarda 2 segundos e imprime em seguida
        (pprint hospital)
        (pprint hospital)
        (pprint futures)))))                                ; devolve 'future', finaliza por retornar o valor ou por ocorrer exception


(simula-um-dia-async)
