(ns loja.aula5
  (:require [loja.db :as l.db])
  (:require [loja.logic :as l.logic]))

; Keep - mantém os valores retornados pela funçã (não recebe um predicado, mas uma função).
; Equivalente a um map + um filter. Retornando nil o item é removido da coleção.
(defn gastou-bastante? [info-usuario]
  (> (:preco-total info-usuario) 500))

(let [pedidos (l.db/todos-pedidos)
      resumo (l.logic/resumo-por-usuario pedidos)]
  (println "> Resumo:" resumo)
  (println "> Keep" (keep gastou-bastante? resumo))
  (println "> Filter" (filter gastou-bastante? resumo)))

(println "----------------------------")

(defn gastou-bastante? [info-usuario]
  (println "gastou-bastante?" (:usuario-id info-usuario))
  (> (:preco-total info-usuario) 500))

(let [pedidos (l.db/todos-pedidos)
      resumo (l.logic/resumo-por-usuario pedidos)]
  (println "Sequência sendo retornada meio embaralhada aparentemente, fora de ordem.")
  (println "> Keep" (keep gastou-bastante? resumo)))

(println "----------------------------")
(println "Isolando o código...")

(println (range 10))
(println (take 2 (range 100)))
(println (take 2 (range 10000000000000000000)))
; A sequência não é gerada por inteiro, de forma "gulosa" (eager), apenas quando necessário

