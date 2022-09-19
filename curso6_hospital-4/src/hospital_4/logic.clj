(ns hospital-4.logic)

; WHEN-LET
; Vantagem, funciona para o caso de departamento não existente no hospital,
; regra mais explícita
#_(defn cabe-na-fila?
    [hospital departamento]
    (when-let [fila (get hospital departamento)]
      (-> hospital
          departamento                                      ; onde 'departamento é um keywork, ex. ":espera hospital"
          count
          (< 5))))                                          ; caso if false retorna nil


; SOME
; Desvantagem, menos explícito e qulquer nil que ocorrer devolve 'nil'
(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital                                          ; vai 'threadiando' se alguém for nulo devolve nilł
          departamento                                      ; obtem nil aqui caso seja passado algum departamento ausente no hospital
          count
          (< 5)))

#_(defn chega-em
    [hospital departamento pessoa]
    (if (cabe-na-fila? hospital departamento)
      (update hospital departamento conj pessoa)
      (throw (ex-info "Não cabe ninguém neste departamento." {:paciente pessoa}))))

; Deveria retornar um hospital para que o vetor seja atualizado com esse valor
(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)))            ; caso true, atualiza; false, reotorna nil
