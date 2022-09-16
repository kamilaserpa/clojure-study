(ns hospital-3.aula5
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int?))
(def Plano [s/Keyword])                                     ; definindo schema do Plano como um vetor de keywords
(def Paciente
  {:id                          PosInt,
   :nome                        s/Str,
   :plano                       Plano,
   (s/optional-key :nascimento) s/Str})

(def PacientesLista
  {PosInt Paciente})                                        ; não usa keyword, por padrão propriedade é opcional

; Habilitamos a garantia de que o Paciente possui id válido com PosInt
; (s/validate Paciente {:id nil :nome "Joe" :plano []})       ; Exception Value does not match schema: {:id (not (pos-int? nil))}

(def VisitasLista
  {PosInt [s/Str]})


(s/defn adiciona-paciente :- PacientesLista
  "Recebe uma lista de pacientes, e um paciente a ser adicionado
  Como a validação do 'id' está ativa não é mais necessário verificação de presença do 'id'"
  [pacientesLista :- PacientesLista
   paciente :- Paciente]
  (let [id (:id paciente)]
    (assoc pacientesLista id paciente)))


(s/defn adiciona-visita :- VisitasLista
  [visitas :- VisitasLista,
   paciente :- PosInt,
   novas-visitas :- [s/Str]]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)          ; if true
    (assoc visitas paciente novas-visitas)))                ; if false

(s/defn imprime-relatorio-de-paciente
  [visitas :- VisitasLista,
   paciente :- PosInt]
  (println "> Visitas do paciente" paciente "são" (get visitas paciente)))

(defn testa-uso-de-pacientes []
  (let
    [guilherme {:id 15 :nome "Guilherme", :plano []}
     daniela {:id 20 :nome "Daniela", :plano []}
     paulo {:id 25, :nome "Paulo", :plano []}

     ; variação com reduce, mais natural
     pacientes (reduce adiciona-paciente {} [guilherme daniela paulo]) ; reduzindo uma colação, chama a fn adiciona paciente passado cada um destes parametros (pessoas) em sequência

     ; variação com shadowing, fica feio
     visitas {}
     visitas (adiciona-visita visitas 15 ["01/01/2019"])
     visitas (adiciona-visita visitas 20 ["01/02/2019", "01/01/2020"])
     visitas (adiciona-visita visitas 15 ["01/03/2019"])]


    (pprint pacientes)
    (pprint visitas)

    (imprime-relatorio-de-paciente visitas 15)
    (imprime-relatorio-de-paciente visitas 20)))
;(imprime-relatorio-de-paciente visitas "testa")))       ; Exception - Input to imprime-relatorio-de-paciente does not match schema:

(testa-uso-de-pacientes)
