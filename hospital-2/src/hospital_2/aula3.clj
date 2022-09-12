(ns hospital_2.aula3
  (:use clojure.pprint)
  (:require [hospital_2.logic :as h.logic]))

(defn carrega-paciente [id]
  (println "Carregando..." id)
  (Thread/sleep 1000)
  {:id id, :carregado-em (h.logic/agora)})

; (pprint (carrega-paciente 15))

; Verifica se o vetor 'pacientes' já contém o id recebido
; se já existe devolve os pacientes (já com o id presente)
; caso contrário carrega o paciente
(defn carrega-se-nao-existe
  [cache id funcao-carregadora]
  (if (contains? cache id)
    cache
    (let [paciente (funcao-carregadora id)]
      (assoc cache id paciente))))                          ; adiciona no mapa 'cache' o identificador id com o conteúdo do paciente

; (pprint (carrega-se-nao-existe {}, 15, carrega-paciente))
; (pprint (carrega-se-nao-existe {15 {:id 15}}, 15, carrega-paciente))

(defprotocol Carregavel
  (carrega! [this id]))

; Classe 'Cache' que implementa o protocol Carregavel e insere corpo à função 'carrega!'
(defrecord Cache
  [cache funcao-carregadora]

  Carregavel
  (carrega! [this id]
    (swap! cache carrega-se-nao-existe id funcao-carregadora)
    (get @cache id)))                                       ; devolvendo o valor do cache


(def pacientes (->Cache (atom {}), carrega-paciente))
(pprint pacientes)

(carrega! pacientes 15)
(carrega! pacientes 30)
(carrega! pacientes 15)
(pprint pacientes)
