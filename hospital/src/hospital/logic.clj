(ns hospital.logic)
; COnsideramos que no hospital há um limite de 5 pessoas por fila


(defn cheio? [hospital departamento]
  (-> hospital                                              ; thread first pois envia o argumento como primeiro parâmetro nas chamadas abaixo
      (get,,, departamento)                                 ; obtem departamento para o hospital
      count,,,                                              ; conta índices do departamento
      (<,,, 5)))                                            ; verifica se é menor que 5


; Adiciona paciente ao final de uma fila de departamento
(defn chega-em
  [hospital departamento pessoa]
  (if (cheio? hospital departamento)
    (update hospital departamento conj pessoa)              ; atualiza o hospital, adiciona (conj) a pessoa no departamento (LaboratorioN)
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa})))) ; lançando exception


; Remove primeiro paciente da fila do departamento
(defn atende
  [hospital departamento]
  (update hospital departamento pop))

; Adiciona paciente ao final de uma fila de departamento
; Simulando vários processamentos em paralelo no servidor
; Erro de imprevisibilidade da ordem de execução
; Função malvada não pura pois utiliza randon trazendo comportamentos diferentes para cada chamada
(defn chega-em-pausado
  [hospital departamento pessoa]
  (Thread/sleep (* (rand) 1000))
  (if (cheio? hospital departamento)
    (do                                                     ;(Thread/sleep (* (rand) 1000))                                 ; utiliza-se 'do' para caos true com mais de um bloco
      (update hospital departamento conj pessoa))
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa}))))


(defn chega-em-pausado-logando
  [hospital departamento pessoa]
  (println "Tentando adicionar a pessoa" pessoa)
  (Thread/sleep (* (rand) 1000))
  (if (cheio? hospital departamento)
    (do (println "Dei o update:" pessoa)
        (update hospital departamento conj pessoa))
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa}))))