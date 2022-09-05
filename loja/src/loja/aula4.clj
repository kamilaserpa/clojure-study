(ns loja.aula4
  (:require [loja.db :as l.db]
            [loja.logic :as l.logic]))

(println (l.db/todos-pedidos))

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
       (sort-by :preco-total)))                             ; ainda não mapeado

(let [pedidos (l.db/todos-pedidos)
      resumo (resumo-por-usuario-ordenado-por-preco pedidos)]
  (println)
  (println "> Resumo:" resumo)
  (println "> Ordenado por preço total asc:" (sort-by :preco-total resumo))
  (println "> Ordenado ao contrário:" (reverse (sort-by :preco-total resumo)))
  (println "> Ordenado por id:" (sort-by :usuario-id resumo))
  (println "> get-in:" (get-in pedidos [0 :itens :mochila :quantidade]))) ; obtém quantidade da mochila do item de índice 0


