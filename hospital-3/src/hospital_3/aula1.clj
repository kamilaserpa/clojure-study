(ns hospital-3.aula1
  (:use clojure.pprint))

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
