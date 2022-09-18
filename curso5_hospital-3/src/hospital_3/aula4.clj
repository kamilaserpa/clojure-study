(ns hospital-3.aula4
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int?))

(def Plano [s/Keyword])                                     ; definindo schema do Plano como um vetor de keywords

(def Paciente
  {:id                          PosInt,
   :nome                        s/Str,
   :plano                       Plano,                      ; se a chave é um keyword 0 ela é obrigatória
   (s/optional-key :nascimento) s/Str})                     ; definindo propriedade   nascimento como opcional

(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x, :ultrasom]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [], :nascimento "18/01/1981"}))

; PacientesLista
; { 15 {PACIENTE} 32 {PACIENTE} }

(def PacientesLista                                         ; PosInt é um símbolo com valor que configura o id, positivo e inteiro
  {PosInt Paciente})                                        ; não usa keyword, por padrão propriedade é opcional

(pprint (s/validate PacientesLista {}))                     ; valido, pois não usa keywork (PosInt), valor opcional

(let [guilherme {:id 15, :nome "Guilherme", :plano [:raio-x, :ultrasom]}
      joe {:id 17, :nome "Joe", :plano []}]
  (pprint (s/validate PacientesLista {15 guilherme}))
  (pprint (s/validate PacientesLista {15 guilherme, 17 joe})))
;(pprint (s/validate PacientesLista {-15 guilherme}))       ; Exception - {(not (pos-int? -15)) invalid-key}
;(pprint (s/validate PacientesLista {15 15}))) )            ; Exception - {15 (not (map? 15))}

(def Visitas
  {PosInt [s/Str]})




