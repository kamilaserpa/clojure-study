(ns hospital_2.aula4
  (:use clojure.pprint))

(defrecord PacienteParticular [id, nome, nascimento, situacao])
(defrecord PacientePlanoDeSaude [id, nome, nascimento, situacao, plano])

; RN - Deve assinar pre-autorizacao?
; valor >= 50, ou não está coberto pelo plano
; não emergencia, em caso de emergencia não necessita pre-autorização

(defprotocol Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]))

(defn nao-eh-urgente? [paciente]
  (not= :urgente (:situacao paciente :normal)))             ; captura a propriedade 'situacao' do paciente, caso nil o valor padrão retornado é 'normal'

(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (and
      (>= valor 50)
      (nao-eh-urgente? paciente))))                         ; copy paste

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (let [plano (:plano paciente)]
      (and
        (not (some #(= % procedimento) plano))
        (nao-eh-urgente? paciente)))))                      ; copy paste em extend-type distinto


(println "> Verificando pacientes:")
(let [particular (->PacienteParticular 15, "Guilherme", "18/09/1981", :normal)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/09/1981", :normal, [:raio-x, :ultrassonografia])]
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? particular, :raio-x, 40))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? plano, :raio-x, 40))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? plano, :ressonancia, 40)))

(println "> Verificando pacientes em emergência:")
(let [particular (->PacienteParticular 15, "Guilherme", "18/09/1981", :urgente)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/09/1981", :urgente, [:raio-x, :ultrassonografia])]
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? particular, :raio-x, 40))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? plano, :raio-x, 40))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao? plano, :ressonancia, 40)))

(println "----------------------------------\n")

; Criando função multipla, com várias implementações
(defmulti deve-assinar-pre-autorizacao-multi? class)        ; utilizará a classe como parâmetro para selecionar a implementação

(defmethod deve-assinar-pre-autorizacao-multi? PacienteParticular [paciente]
  (println "Invocando multi PacienteParticular")
  true)

(defmethod deve-assinar-pre-autorizacao-multi? PacientePlanoDeSaude [paciente]
  (println "Invocando multi PacientePlanoDeSaude")
  false)

(println "> Verificando pacientes (com defmulti):")
(let [particular (->PacienteParticular 15, "Guilherme", "18/09/1981", :urgente)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/09/1981", :urgente, [:raio-x, :ultrassonografia])]
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao-multi? particular))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao-multi? particular))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao-multi? plano))
  (println "Deve assinar pre-autorização?" (deve-assinar-pre-autorizacao-multi? plano)))

(println "----------------------------------\n")
; Na verdade estamos enviando um mapa (paciente) como parâmetro e não uma tipagem de classe
; (defmulti deve-assinar-pre-autorizacao-multi? class)

; Explorando defmulti
(defn minha-funcao [p]
  (println p)
  (class p))                                                ; devolve a classe de 'p'

(defmulti multi-teste minha-funcao)
;(multi-teste "kamila")
;(multi-teste :guitarrista)

(println "---------------------------------\n")

; pedido { :paciente paciente, :valor valor, :procedimento procedimento }

; Um pouco feio, misturando keywork e classe como chave
; Funcao testável, habilitando polimorfismo por um valor específico (sempre-autorizado) e por classes (PacienteParticular e PacientePlanoDeSaude)
(defn tipo-de-autorizador [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)
        urgencia? (= :urgente situacao)]
    (if urgencia?
      :sempre-autorizado
      (class paciente))))

(defmulti deve-assinar-pre-autorizacao-do-pedido? tipo-de-autorizador)

; Implementações de acordo com o retornado pela função 'tipo-de-autorizacao'
(defmethod deve-assinar-pre-autorizacao-do-pedido? :sempre-autorizado [pedido]
  false)

(defmethod deve-assinar-pre-autorizacao-do-pedido?
  PacienteParticular [pedido]
  (>= (:valor pedido 0) 50))

(defmethod deve-assinar-pre-autorizacao-do-pedido?
  PacientePlanoDeSaude [pedido]
  (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))

;
(println "> Verificando pacientes com urgência (com defmulti):")
(let [particular (->PacienteParticular 15, "Guilherme", "18/09/1981", :urgente)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/09/1981", :urgente, [:raio-x, :ultrassonografia])]
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente particular, :valor 1000, :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))

(println "> Verificando pacientes (com defmulti):")
(let [particular (->PacienteParticular 15, "Guilherme", "18/09/1981", :normal)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/09/1981", :normal, [:raio-x, :ultrassonografia])]
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente particular, :valor 1000, :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente plano, :valor 1000, :procedimento :raio-x}))
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))

