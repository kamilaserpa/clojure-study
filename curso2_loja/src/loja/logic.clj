(ns loja.logic)

; Isolando lógica criada na aula3
(defn total-do-item
  [[_ detalhes]]
  (* (get detalhes :quantidade 0) (get detalhes :preco-unitario 0)))


(defn total-do-pedido
  [pedido]
  (->> pedido
       (map total-do-item)
       (reduce +)))

; Somatório dos vários pedidos de um mesmo usuário
(defn total-dos-pedidos
  [pedidos]
  (->> pedidos                                              ; passa 'pedidos' como último argumento para as fn a seguir
       (map :itens)                                         ; extrai os itens do pedido
       (map total-do-pedido)                                ; calcula o total de cada pedido
       (reduce +)))                                         ; soma todos os totais

; Retornando mapa estruturado, estabelecendo padrão
(defn quantia-de-pedidos-e-gasto-total-por-usuario
  [[usuario pedidos]]
  {:usuario-id       usuario
   :total-de-pedidos (count pedidos)
   :preco-total      (total-dos-pedidos pedidos)})          ; delega à fn 'total-dos-pedidos' o cálculo

(defn resumo-por-usuario [pedidos]
  (->> pedidos
       (group-by :usuario)
       (map quantia-de-pedidos-e-gasto-total-por-usuario)))
