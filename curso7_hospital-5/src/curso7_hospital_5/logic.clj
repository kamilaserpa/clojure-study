(ns curso7-hospital-5.logic
  (:require [curso7-hospital-5.model :as h.model]
            [schema.core :as s]))


(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
          departamento
          count
          (< 5)))

(defn- tenta-colocar-na-fila
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa}))))

(s/defn atende :- h.model/Hospital
  "Remove o primeiro paciente da fila de um departamento"
  [hospital :- h.model/Hospital, departamento :- s/Keyword]
  (update hospital departamento pop))

(s/defn proximo :- h.model/PacienteID
  "Retorna o próximo (primeiro) paciente da fila de um departamento"
  [hospital :- h.model/Hospital, departamento :- s/Keyword]
  (-> hospital
      departamento
      peek))

; Função de pós condição extraída possibilitando ser testada individualmente
(defn mesmo-tamanho?
  [hospital, outro-hospital, departamento-origem, departamento-destino]
  (= (+ (count (get outro-hospital departamento-origem)) (count (get outro-hospital departamento-destino)))
     (+ (count (get hospital departamento-origem)) (count (get hospital departamento-destino)))))


(s/defn transfere :- h.model/Hospital
  "Remove o primeiro paciente da fila de espera e o adiciona na fila de um laboratório"
  [hospital :- h.model/Hospital, departamento-origem :- s/Keyword, departamento-destino :- s/Keyword]
  {
   :pre  [(contains? hospital departamento-origem), (contains? hospital departamento-destino)] ; AssertionError caso false, opcional em tempo de execução, pode ser desligado em execução
   :post [(mesmo-tamanho? hospital % departamento-origem departamento-destino)]}

  (let [pessoa (proximo hospital departamento-origem)]
    (-> hospital
        (atende departamento-origem)
        (chega-em departamento-destino pessoa))))

(defn total-de-pacientes [hospital]
  (reduce + (map count (vals hospital))))