(ns hospital-3.aula3
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int?))

(def Paciente
  {:id PosInt, :nome s/Str})

(s/defn novo-paciente :- Paciente
  [id :- PosInt,
   nome :- s/Str]
  {:id id, :nome nome})

(pprint (novo-paciente 15, "Joe"))
; (pprint (novo-paciente -5, "Joe"))                          ; Exception

(defn maior-ou-igual-a-zero? [x] (>= x 0))

; Schema aceito é um valor numérico e maior ou igual a zero
(def ValorFinanceiro (s/constrained s/Num maior-ou-igual-a-zero?))

(def Pedido
  {:paciente     Paciente
   :valor        ValorFinanceiro
   :procedimento s/Keyword})

(s/defn novo-pedido :- Pedido
  [paciente :- Paciente,
   valor :- ValorFinanceiro,
   procedimento :- s/Keyword]
  {:paciente paciente, :valor valor, :procedimento procedimento})

(println "Novo-pedido:" (novo-pedido (novo-paciente 15, "Guilherme"), 15.53, :raio-x))
; (println "Novo-pedido:" (novo-pedido (novo-paciente 15, "Guilherme"), 15.53, "raio-x")) ; Exception
; (println "Novo-pedido:" (novo-pedido (novo-paciente 15, "Guilherme"), -15.53, :raio-x)) ; Exception

(println "-------- Schemas de Sequências ------------")

(def Numeros [s/Num])
(pprint (s/validate Numeros [15]))
(pprint (s/validate Numeros [17, 15, 23.2, 0]))
(pprint (s/validate Numeros [0]))
(println "[]:" (s/validate Numeros []))
(println "nil:" (s/validate Numeros nil))                   ; nil é tratado como uma sequência vazia
;(pprint (s/validate Numeros [nil]))                         ; Exception

(println "[s/Num]:" (s/validate [s/Num] nil))               ; nil é válido como um vetor vazio
; (pprint (s/validate s/Num nil))                             ; nil não é valido como "Num"

(def Plano [s/Keyword])                                     ; definindo schema do Plano como um vetor de keywords
(pprint (s/validate Plano [:raio-x]))

(def Paciente {:id PosInt, :nome s/Str, :plano Plano})
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x, :ultrasom]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano []}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano nil}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme"}))  ; Exception - {:plano missing-required-key}
