(ns hospital.aula6
  (:use [clojure pprint])
  (:require [hospital.model :as h.model]))

(defn chega-em [fila pessoa]
  (conj fila pessoa))

; Garante que se durante a transação o valor da fila for alterado ocorre o retry
(defn chega-em!
  "Troca de referêncai via ref-set"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (ref-set fila (chega-em @fila pessoa))))

(defn chega-em!
  "Troca de referência via alter"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (alter fila chega-em pessoa)))

(defn simula-um-dia []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]

    (dosync (chega-em! hospital "guilherme"))
    (pprint hospital)))

(simula-um-dia)
