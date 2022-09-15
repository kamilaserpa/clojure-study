(ns hospital-3.aula3
  (:use clojure.pprint)
  (:require [schema.core :as s])
  (:import (javax.security.auth PrivateCredentialPermission$CredOwner)))

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

(println "Novo-pedido 1:" (novo-pedido (novo-paciente 15, "Guilherme"), 15.53, :raio-x))
; (println "Novo-pedido 1:" (novo-pedido (novo-paciente 15, "Guilherme"), 15.53, "raio-x")) ; Exception
; (println "Novo-pedido 1:" (novo-pedido (novo-paciente 15, "Guilherme"), -15.53, :raio-x)) ; Exception