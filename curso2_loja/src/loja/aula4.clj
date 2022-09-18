(ns loja.aula4
  (:require [loja.db :as l.db]
            [loja.logic :as l.logic]))

(println (l.db/todos-pedidos))
(println "-----------------------")

(let [pedidos (l.db/todos-pedidos)
      resumo (l.logic/resumo-por-usuario pedidos)]
  (println "> Resumo:" resumo)
  (println "> Ordenado por preço total asc:" (sort-by :preco-total resumo))
  (println "> Ordenado ao contrário:" (reverse (sort-by :preco-total resumo)))
  (println "> Ordenado por id:" (sort-by :usuario-id resumo))
  (println "> get-in:" (get-in pedidos [0 :itens :mochila :quantidade]))) ; obtém quantidade da mochila do item de índice 0

(println "-----------------------")


(defn resumo-por-usuario-ordenado-por-preco
  [pedidos]
  (->> pedidos
       l.logic/resumo-por-usuario                           ; mapeando para atributos do 'resumo'
       (sort-by :preco-total)                               ; ordenando pelo atributo preco-total
       reverse))                                            ; ordanando do maior para o menor

(defn top-2 [resumo]
  (take 2 resumo))

(let [pedidos (l.db/todos-pedidos)
      resumo (resumo-por-usuario-ordenado-por-preco pedidos)]
  (println "> Resumo:" resumo)
  (println "> Primeiro:" (first resumo))
  (println "> Segundo:" (second resumo))
  (println "> Resto:" (rest resumo))
  (println "> Quantidade Total:" (count resumo))
  (println "> Class:" (class resumo))
  (println "> nth 1:" (nth resumo 1))                       ; retorna objeto de índice 1, get não funciona pois não temos um vetor, mas uma coleção
  (println "> take:" (take 2 resumo))                       ; retorna os dois primeiros elementos
  (println "> top-2:" (top-2 resumo)))                      ; retorna os dois primeiros elementos

(println "-----------------------")


(let [pedidos (l.db/todos-pedidos)
      resumo (resumo-por-usuario-ordenado-por-preco pedidos)]
  (println "Quais gastaram > 500 R$ (filter)?" (filter #(> (:preco-total %) 500) resumo)) ; funcao anonima. Captura atributo tributo :preco-total do argumento passado (%)
  (println "Alguém gastou > 500 R$ (filter)?" (not (empty? (filter #(> (:preco-total %) 500) resumo))))
  (println "Alguém gastou > 500 R$ (some)?" (some #(> (:preco-total %) 500) resumo)) ; retorna true ou nil
  (println "Alguém gastou > 500 R$ (some)?" (some #(> (:preco-total %) 50000) resumo)))
