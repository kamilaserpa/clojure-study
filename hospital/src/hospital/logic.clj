(ns hospital.logic)
; Consideramos que no hospital há um limite de 5 pessoas por fila


(defn cabe-na-fila? [hospital departamento]
  (-> hospital                                              ; thread first pois envia o argumento como primeiro parâmetro nas chamadas abaixo
      (get,,, departamento)                                 ; obtem departamento para o hospital
      count,,,                                              ; conta índices do departamento
      (<,,, 5)))                                            ; verifica se é menor que 5


; Adiciona paciente no final da fila de um departamento
(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)              ; atualiza o hospital, adiciona (conj) a pessoa no departamento (LaboratorioN)
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa})))) ; lançando exception


; Remove o primeiro paciente da fila de um departamento
(defn atende
  [hospital departamento]
  (update hospital departamento pop))


(defn proximo [hospital departamento]
  "Retorna o próximo (primeiro) paciente da fila de um departamento"
  (-> hospital
      departamento
      peek))

; Remove o primeiro paciente da fila de espera e adiciona na fila de um laboratório
(defn transfere [hospital departamento-origem departamento-destino]
  (let [pessoa (proximo hospital departamento-origem)]
    (-> hospital
        (atende departamento-origem)
        (chega-em departamento-destino pessoa))))





; Adiciona paciente ao final de uma fila de departamento
; Simulando vários processamentos em paralelo no servidor
; Erro de imprevisibilidade da ordem de execução
; Função malvada não pura pois utiliza randon trazendo comportamentos diferentes para cada chamada
#_
(defn chega-em-pausado
  [hospital departamento pessoa]
  (Thread/sleep (* (rand) 1000))
  (if (cabe-na-fila? hospital departamento)
    (do                                                     ;(Thread/sleep (* (rand) 1000))                                 ; utiliza-se 'do' para caos true com mais de um bloco
      (update hospital departamento conj pessoa))
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa}))))

#_
(defn chega-em-pausado-logando
  [hospital departamento pessoa]
  (println "Tentando adicionar a pessoa" pessoa)
  (Thread/sleep (* (rand) 1000))
  (if (cabe-na-fila? hospital departamento)
    (do (println "Dei o update:" pessoa)
        (update hospital departamento conj pessoa))
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa}))))