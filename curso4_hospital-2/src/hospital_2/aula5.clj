(ns hospital_2.aula5
  (:use clojure.pprint))

(defrecord PacienteParticular [id, nome, nascimento, situacao])
(defrecord PacientePlanoDeSaude [id, nome, nascimento, situacao, plano])

(defn tipo-de-autorizador [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)]
    (cond (= :urgente situacao) :sempre-autorizado          ; condições
          (contains? paciente :plano) :plano-de-saude       ; autorizador do plano de saúde
          :else :credito-minimo)))

(defmulti deve-assinar-pre-autorizacao? tipo-de-autorizador)

(defmethod deve-assinar-pre-autorizacao? :sempre-autorizado [pedido]
  false)

(defmethod deve-assinar-pre-autorizacao? :plano-de-saude [pedido]
  (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))

(defmethod deve-assinar-pre-autorizacao? :credito-minimo [pedido]
  (>= (:valor pedido 0) 50))                                ; só se o valor do procedimento for 50 R$ ou mais

(println "> Verificando pacientes com urgência (com defmulti):")
(let [particular {:id 15, :nome "Guilherme", :nascimento "18/09/1981", :situacao :urgente}
      plano {:id 15, :nome "Guilherme", :nascimento "18/09/1981", :situacao :urgente, :plano [:raio-x, :ultrasonografia]}]
  (pprint (deve-assinar-pre-autorizacao? {:paciente particular, :valor 1000, :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar-pre-autorizacao? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))

(println "> Verificando pacientes sem urgência (com defmulti):")
(let [particular {:id 15, :nome "Guilherme", :nascimento "18/09/1981", :situacao :normal}
      plano {:id 15, :nome "Guilherme", :nascimento "18/09/1981", :situacao :normal, :plano [:raio-x, :ultrasonografia]}]
  (pprint (deve-assinar-pre-autorizacao? {:paciente particular, :valor 1000, :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar-pre-autorizacao? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar-pre-autorizacao? {:paciente plano, :valor 1000, :procedimento :raio-x})))
