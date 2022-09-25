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
#_(defn chega-em
    [hospital departamento pessoa]
    (if (cabe-na-fila? hospital departamento)
      (update hospital departamento conj pessoa)))          ; caso true, atualiza; false, reotorna nil

; Para verificação de ex-data
#_(defn chega-em
    [hospital departamento pessoa]
    (if (cabe-na-fila? hospital departamento)
      (update hospital departamento conj pessoa)
      (throw (ex-info "Não cabe ninguém neste departamento." {:paciente pessoa, :tipo :impossivel-colocar-pessoa-na-fila}))))

(defn- tenta-colocar-na-fila
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)))

(defn chega-em [hospital departamento pessoa]
  (if-let [novo-hospital (tenta-colocar-na-fila hospital departamento pessoa)]
    {:hospital novo-hospital, :resultado :sucesso}
    {:hospital hospital, :resultado :impossivel-colocar-na-fila})) ; retorna o hospital como estava antes

; Com a abordagem de retornar o hospital e o status de resultado, teria de haver outra função
; para tratar e capturar o conteúdo do novo hospital, ou tratativa caso não seja possível adicionar uma pessoa,
; ou seja, manipular o resultado antes de fazer swap
; (defn chega-em! [hospital departamento pessoa]
;  (:hospital (chega-em hospital departamento pessoa)))