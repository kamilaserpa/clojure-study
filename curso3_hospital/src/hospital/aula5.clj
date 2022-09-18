(ns hospital.aula5
  (:use [clojure pprint])
  (:require [hospital.logic :as h.logic]
            [hospital.model :as h.model]))

; Quem chega ao hospital chega sempre na fila de Espera
(defn chega-em! [hospital pessoa]
  (swap! hospital h.logic/chega-em :espera pessoa))

; Remove o paciente
(defn transfere! [hospital de para]
  (swap! hospital h.logic/transfere de para))

(defn simula-um-dia []
  (let [hospital (atom (h.model/novo-hospital))]
    (chega-em! hospital "João")
    (chega-em! hospital "Maria")
    (chega-em! hospital "Daniela")
    (chega-em! hospital "Guilherme")

    (transfere! hospital :espera :laboratorio1)
    (transfere! hospital :espera :laboratorio2)
    (transfere! hospital :espera :laboratorio2)
    (transfere! hospital :laboratorio2 :laboratorio3)

    (pprint hospital)))

(simula-um-dia)

