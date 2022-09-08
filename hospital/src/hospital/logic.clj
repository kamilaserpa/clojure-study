(ns hospital.logic)

; Adiciona paciente ao final de uma fila de departamento
(defn chega-em
  [hospital departamento pessoa]
  (update hospital departamento conj pessoa))               ; atualiza o hospital, adiciona (conj) a pessoa no departamento (LaboratorioN)


; Remove primeiro paciente da fila do departamento
(defn atende
  [hospital departamento]
  (update hospital departamento pop))



