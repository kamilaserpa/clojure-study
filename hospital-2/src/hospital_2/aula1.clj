(ns hospital_2.aula1
  (:use clojure.pprint))

#_(defn adiciona-paciente
    "Os pacientes são um mapa: {15 {paciente 15}, 23 {paciente 23}
O paciente { :id 15 ...."
    [pacientes paciente]
    (let [id (:id paciente)]
      (if id
        (assoc pacientes id paciente)
        (throw (ex-info "Paciente não possui 'id'" {:paciente paciente}))))) ; se refere ao argumento "id" criado no let


; Semelhante a fn anterior com IF-LET
(defn adiciona-paciente
  "Os pacientes são um mapa: {15 {paciente 15}, 23 {paciente 23}
  O paciente { :id 15 ...."
  [pacientes paciente]
  (if-let [id (:id paciente)]                               ; if id, se possui id, realiza o bloco seguinte (assoc)
    (assoc pacientes id paciente)                           ; se refere ao argumento "id" criado no if-let
    (throw (ex-info
             "Paciente não possui 'id'"
             {:paciente paciente}))))


(defn testa-adiciona-paciente []
  (let [pacientes {}
        guilherme {:id 15 :nome "Guilherme" :nascimento "18/09/1981"}
        daniela {:id 20 :nome "Daniela" :nascimento "18/09/1982"}
        paulo {:nome "Paulo" :nascimento "18/09/1983"}]     ; testando paciente sem id

    (pprint (adiciona-paciente pacientes guilherme))
    (pprint (adiciona-paciente pacientes daniela))
    (pprint (adiciona-paciente pacientes paulo))))          ; associa paciente com id = nil

(testa-adiciona-paciente)
