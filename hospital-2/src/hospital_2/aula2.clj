(ns hospital_2.aula2
  (:use clojure.pprint))

(defrecord Paciente [id, nome, nascimento])

; Paciente Plano de Saúde ===> + plano de saúde
; Paciente Particular ===> +

(defrecord PacienteParticular [id, nome, nascimento])
(defrecord PacientePlanoDeSaude [id, nome, nascimento, plano])

; REGRAS DIFERENTES PARA TIPOS DIFERENTES
; deve-assinar-pre-autorizacao?
; Particular ===> valor do procedimento >= 50
; PlanoDeSaude ===> procedimento NÃO está coberto pelo plano


; FUNÇÃO BASEADA NO TIPO
; Ideia de interface em java
(defprotocol Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]))

; Ideia de implementacao de Interface.
; Extendendo um tipo (PacienteParticular), que implementa um protocolo (Cobravel)
(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (>= valor 50)))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (let [plano (:plano paciente)]                          ; obtém plano do paciente
      (not (some #(= % procedimento) plano)))))             ; não existe nenhum procedimento no plano que seja semelhante ao que se deseja

(let [particular (->PacienteParticular 15, "Guilherme", "18/09/1981")
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/09/1981", [:raio-x, :ultrassonografia])]
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? particular, :raio-x, 40))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? plano, :raio-x, 40))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? plano, :ressonancia, 40)))

