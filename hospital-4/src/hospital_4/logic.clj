(ns hospital-4.logic)

(defn cabe-na-fila?
  [hospital departamento]
  (-> hospital
      departamento                                          ; onde 'departamento é um keywork, ex. ":espera hospital"
      count
      (< 5)))
