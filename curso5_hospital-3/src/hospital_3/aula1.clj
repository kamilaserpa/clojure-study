(ns hospital-3.aula1
  (:use clojure.pprint)
  (:require [schema.core :as s]))                           ;; cljs only


(defn adiciona-paciente [pacientes paciente]                ; recebe uma lista de pacientes, e um paciente a ser adicionado
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente não possui id" {:paciente paciente}))))

; { 15 [], 20 [], 25 [] } - identificador do paciente com vetor de visitas
(defn adiciona-visita [visitas, paciente, novas-visitas]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))

(defn imprime-relatorio-de-paciente [visitas, paciente]
  (println "Visitas do paciente" paciente "são" (get visitas paciente)))

(defn testa-uso-de-pacientes []
  (let
    [guilherme {:id 15 :nome "Guilherme"}
     daniela {:id 20 :nome "Daniela"}
     paulo {:id 25, :nome "Paulo"}

     ; variação com reduce, mais natural
     pacientes (reduce adiciona-paciente {} [guilherme daniela paulo]) ; reduzindo uma colação, chama a fn adiciona paciente passado cada um destes parametros (pessoas) em sequencia

     ; variação com shadowing, fica feio
     visitas {}
     visitas (adiciona-visita visitas 15 ["01/01/2019"])
     visitas (adiciona-visita visitas 20 ["01/02/2019", "01/01/2020"])
     visitas (adiciona-visita visitas 15 ["01/03/2019"])]
    (pprint pacientes)
    (pprint visitas)                                        ;{15 ("01/01/2019" "01/03/2019"), 20 ["01/02/2019" "01/01/2020"]}

    ; Problemas grandes por utilizar o símbolo 'paciente' em diversos lugares do sistema com significados diferentes
    ; ora id, ora objeto paciente completo
    (imprime-relatorio-de-paciente visitas guilherme)
    (println (get visitas 15))))

(testa-uso-de-pacientes)

(println "---------------SCHEMA--------------\n")

; Usando Schema
(pprint (s/validate Long 15))
; (pprint (s/validate Long "Kamila"))                         ; erro, "Kamila" não é uma instância de Long

(s/set-fn-validation! true)                                 ; habilita a checagem em tempo de execução

(s/defn teste-simples [x :- Long]                           ; ":-" indica que x segue o schema Long
  (println x))

(teste-simples 30)
; (teste-simples "Luiza")                                     ; Exception, Luiza is not Long

(s/defn imprime-relatorio-de-paciente
  [visitas, paciente :- Long]
  (println "Visitas do paciente" paciente "são" (get visitas paciente)))

; Agora conseguimos o erro em tempo de *execução* que informa
; que o valor passado como parâmetro não condiz com o schema Long
; (testa-uso-de-pacientes) ; Exception

(s/defn novo-paciente
  [id :- Long, nome :- s/Str]
  {:id id, :nome nome})

(pprint (novo-paciente 15 "Guilherme"))
(pprint (novo-paciente 17 "Joe"))

(println "-------------- Testando schema ---------------")

(s/defn foo :- s/Num                                        ; schema de retorno da fn Num
  [x :- s/Int                                               ; schema do símbolo x
   y :- s/Num]
  (* x y))

(s/defn hi-text
  [x :- String]
  "Hi!")

(s/defn hi-number :- String
  [x]                                                       ; Define schema de retorno como String, mas está retornando um number
  x)

(pprint (foo 2 2.5))                                        ; 5.0
; (pprint (foo 2.5 4))                                        ; Exception, 2.5 not integer
; (pprint (foo (foo 2 2.3) 1.5))                              ; Exception, 2.6 not integer
; (pprint (hi-text (foo 2 3)))                                ; Exception, 6 not String
; (pprint (hi-number 3))                                      ; Exception, 'Output of hi-number does not match schema', 3 not String


(println "Schemas da fn 'foo':" (s/fn-schema foo))          ; Obtém schemas da função
(println "Schemas da fn 'hi-number':" (s/fn-schema hi-number))

(pprint (s/with-fn-validation (foo 1 2)))                   ; verificação em tempo de execução de entradas de função e saídas.
(s/with-fn-validation (foo 1.5 2))                          ; Exception Input to foo does not match schema, 1.5 is not integer
