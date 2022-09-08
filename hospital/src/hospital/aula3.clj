(ns hospital.aula3
  (:use [clojure pprint])
  (:require [hospital.logic :as h.logic]
            [hospital.model :as h.model]))

; símbolo que qualquer thread que acessar esse namespace acessa o valor aqui atribuído "guilherme"
(def nome "guilherme")

(defn testa-atomao []
  (let [hospital-silveira (atom {:espera h.model/fila-vazia})]
    (println hospital-silveira)
    (pprint hospital-silveira)

    (pprint (deref hospital-silveira))                      ; derreferenciar, obter valor dentro do mapa
    (pprint @hospital-silveira)

    (pprint (assoc @hospital-silveira :laboratorio2 h.model/fila-vazia)) ; não altera o conteúdo dentro do átomo
    (pprint @hospital-silveira)

    (swap! hospital-silveira assoc :laboratorio1 h.model/fila-vazia) ;swap altera o conteúdo dentro do átomo
    (pprint @hospital-silveira)

    ; update tradicional imutável, com derreferência, que não trará efeito colateral
    (update @hospital-silveira :laboratorio1 conj "111")

    (swap! hospital-silveira update :laboratorio1 conj "111")
    (pprint @hospital-silveira)))

(testa-atomao)
