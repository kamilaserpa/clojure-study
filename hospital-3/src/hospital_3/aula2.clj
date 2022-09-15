(ns hospital-3.aula2
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

;(s/defrecord Paciente
;  [id :- Long, nome :- s/Str])

; Instancias abaixo são criadas sem exception
; Foward compatible - sistemas externos não 'quebrarão' minha aplicação
; (pprint (Paciente. 15 "Guilherme"))
; (pprint (Paciente. "15" "Guilherme"))
; (pprint (map->Paciente {"15" "Guilherme"}))                 ; funcionamento não desejado, a chave 15 tem valor "Guilherme", record é mais flexível

(def Paciente
  "Schema de um paciente"
  {:id s/Num, :nome s/Str})

(pprint (s/explain Paciente))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme"}))

; typo é pego pelo schema, mas poderíamos argumentar que esse
; tipo de erro seria pego em testes automatizados com boa cobertura
; (pprint (s/validate Paciente {:id 15, :name "Guilherme"}))  ; {:nome missing-required-key, :name disallowed-key}

; Foward Compatible - sistemas externos não me quebrarão ao adicionar campos novos
; Não estamos sendo Foward Compatible (caso erro recebemos exception)
; (pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x]})) ; {:plano disallowed-key}

; Chaves que são keywords em schemas são por padrão OBRIGATÓRIAS
; (pprint (s/validate Paciente {:id 15}))                     ; {:nome missing-required-key}

(s/defn novo-paciente
  [id :- Long, nome :- s/Str]
  {:id id, :nome nome})

(pprint (s/explain Paciente))

; Typo é pego pelo schema. Boa cobertura de testes automatizados deveria capturar esse caso.
; (pprint (s/validate Paciente {:id 15, :name "Guilherme"}))   ; {:nome missing-required-key, :name disallowed-key}

; NÃO Foward Compatible, se outro sistema enviar uma
; estrutura desconhecida resultará em Exception
; (pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x]})) ;{:plano disallowed-key}

; Chaves que são Keywords em schemas são, por padrão, OBRIGATÓRIAS
; (pprint (s/validate Paciente {:id 15}))                     ; {:nome missing-required-key}


; Resulta em Exception, pois o tipo de retorno "Paciente" não deve possuir a propriedade "plano"
; 'Output of novo-paciente does not match schema: {:plano disallowed-key}'
#_(s/defn novo-paciente :- Paciente
    [id :- s/Num, nome :- s/Str]
    {:id id, :nome nome, :plano []})

(s/defn novo-paciente :- Paciente                           ; declaramos o typo de retorno da função
  [id :- s/Num, nome :- s/Str]
  {:id id, :nome nome})

(pprint (novo-paciente 15 "Guilherme"))

(println "--------------------------------------------")

; Função pura, simples, fácil de testar, além da verificação de tipagem
(defn estritamente-positivo? [x]
  (> x 0))

(println "* Cria schema de validação custom *")
; Schemas devem ter padrão CamelCase
; (def EstritamentePositivo (s/pred estritamente-positivo?))
(def EstritamentePositivo (s/pred estritamente-positivo? 'estritamente-positivo')) ; adicionando scape, exibido na mensagem de exception

(pprint (s/validate EstritamentePositivo 15))
; (pprint (s/validate EstritamentePositivo 0))                ; Exception - Value does not match schema
; (pprint (s/validate EstritamentePositivo -15))              ; Exception - Value does not match schema


(println "--------------------------------------------")
(println "* Realiza mais de uma validação por schema *")
(def Paciente
  "Schema de um paciente"
  {:id (s/constrained s/Int pos?), :nome s/Str})            ; pos - validação para numero positivo

(pprint (s/validate Paciente {:id 15, :nome "Joe"}))
; (pprint (s/validate Paciente {:id -15, :nome "Joe"}))       ; Exception - Value does not match schema


; Validando com função anônima, muito ruim de ler e verificar uma mensagem de erro que ocorrer
; "Value does not match schema: {:id (not (hospital-3.aula2/fn--6372 0))}"
; Não fazer dessa maneira, legibilidade prejudicada
(def PacienteTeste1
  "Schema de um paciente com lambda"
  {:id (s/constrained s/Int #(> % 0)), :nome s/Str})

;(pprint (s/validate PacienteTeste1 {:id -0, :nome "Julia"}))


; Não fazer dessa maneira, legibilidade prejudicada
(def PacienteTeste1
  "Schema de um paciente com lambda"
  {:id (s/constrained s/Int #(> % 0) 'estritamente-positivo), :nome s/Str})

; (pprint (s/validate PacienteTeste1 {:id -0, :nome "Julia"}))


(println "* Experimentações *")

; É ímpar?
(pprint (s/validate (s/pred odd?) 5))                       ; 5
; (pprint (s/validate (s/pred))) odd?) 2))                       ; Value does not match schema: (not (odd? 2)

(s/defschema OddLong (s/constrained long odd?))             ; valida se é long e ímpar
(s/validate OddLong 1)                                      ; pass
(s/validate OddLong "try")                                  ; Exception - Value does not match schema: (not (instance? java.lang.Long "try"))
(s/validate OddLong 2)                                      ; Exception - Value does not match schema: (not (odd? 2))
