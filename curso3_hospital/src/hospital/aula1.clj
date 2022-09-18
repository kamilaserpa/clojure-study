(ns hospital.aula1
  (:use [clojure pprint])
  (:require [hospital.model :as h.model]
            [hospital.logic :as h.logic]))

(defn simula-um-dia []
  (def hospital (h.model/novo-hospital))                    ; hospital - símbolo global dentro do namespace, como root binding (valor padrão) 'novo-hospital'
  (def hospital (h.logic/chega-em hospital :espera "111"))
  (def hospital (h.logic/chega-em hospital :espera "222"))
  (def hospital (h.logic/chega-em hospital :espera "333"))
  (pprint hospital)

  (def hospital (h.logic/chega-em hospital :laboratorio1 "444"))
  (def hospital (h.logic/chega-em hospital :laboratorio3 "555"))
  (pprint hospital)

  (def hospital (h.logic/atende hospital :espera))
  (def hospital (h.logic/atende hospital :laboratorio1))
  (pprint hospital)

  (def hospital (h.logic/chega-em hospital :espera "666"))
  (def hospital (h.logic/chega-em hospital :espera "777"))
  (def hospital (h.logic/chega-em hospital :espera "888"))
  (def hospital (h.logic/chega-em hospital :espera "999"))
  (pprint hospital))

;(simula-um-dia)

(println "-----------Demonstrando problema de variável global-----------")
; Em espaços compartilhados, com Thrads concorrentes há problemas como a imprevisibilidade da ordem de execução

(defn chega-em-malvado [pessoa]
  (def hospital (h.logic/chega-em-pausado hospital :espera pessoa))
  (println "> Após inserir" pessoa))

(defn simula-um-dia-em-paralelo []
  (def hospital (h.model/novo-hospital))
  (.start (Thread. (fn [] (chega-em-malvado "111"))))       ; cria um objeto em java que é uma Thread e chama o método .start do objeto
  (.start (Thread. (fn [] (chega-em-malvado "222"))))
  (.start (Thread. (fn [] (chega-em-malvado "333"))))
  (.start (Thread. (fn [] (chega-em-malvado "444"))))
  (.start (Thread. (fn [] (chega-em-malvado "555"))))
  (.start (Thread. (fn [] (chega-em-malvado "666"))))
  (.start (Thread. (fn [] (Thread/sleep 4000)
                     (pprint hospital)))))

(simula-um-dia-em-paralelo)

