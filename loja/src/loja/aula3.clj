(ns loja.aula3
  (:require [loja.db :as l.db]))

(println "> todos-pedidos:" (l.db/todos-pedidos))

(println "> group-by:" (group-by :usuario (l.db/todos-pedidos)))

(defn minha-funcao-de-agrupamento
  [elemento]
  (println "elemento:" elemento)
  (:usuario elemento))

(println "> group-by minha-funcao" (group-by minha-funcao-de-agrupamento
                                             (l.db/todos-pedidos)))

; { 15 []
;    1 []
;   10 []
;   20 [] }

(println "> count" (count (vals (group-by :usuario (l.db/todos-pedidos)))))
(println "> vals" (vals (group-by :usuario (l.db/todos-pedidos))))
; Código  abaixo funciona, mas é uma sequência de invocações ilegível
(println "> map count" (map count (vals (group-by :usuario (l.db/todos-pedidos)))))
(println "-----------------")

; Thread-last
#_(->> (l.db/todos-pedidos)
       (group-by :usuario)
       vals                                                 ; captura vals e ignora keys
       (map count)
       println)

;
(defn conta-total-por-usuario
  [[usuario pedidos]]
  (count pedidos))

(->> (l.db/todos-pedidos)
     (group-by :usuario)
     (map conta-total-por-usuario)
     println)
(println "-----------------")

; Retornando vetor, estranho pois tenta-se criar estrutura em um vetor
(defn conta-total-por-usuario
  [[usuario pedidos]]
  [usuario (count pedidos)])

(->> (l.db/todos-pedidos)
     (group-by :usuario)
     (map conta-total-por-usuario)
     println)
(println "-----------------")

; Retornando mapa com quantidade total de pedidos por usuário
(defn conta-total-por-usuario
  [[usuario pedidos]]
  {:usuario-id       usuario
   :total-de-pedidos (count pedidos)})

(->> (l.db/todos-pedidos)
     (group-by :usuario)
     (map conta-total-por-usuario)
     println)
(println "-----------------")

; Retornando valor total dos pedidos por usuário

(println "PEDIDOS")
; Um item de um pedido do usuário
; {:id :mochila, :quantidade 2, :preco-unitario 80}
; -> total-do-tem = 160
(defn total-do-item
  [[_ detalhes]]                                            ; destruct ignorando o primeiro valor do vetor
  (* (get detalhes :quantidade 0) (get detalhes :preco-unitario 0)))


; Itens de um pedido do usuário:
; { :itens   {:mochila  {:id :mochila, :quantidade 2, :preco-unitario 80}
;                        :camiseta {:id :camiseta, :quantidade 3, :preco-unitario 40}
;                        :tenis    {:id :tenis, :quantidade 1}}}) }
; total-do-pedido = 160 + 120 + 0 = 280
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

(->> (l.db/todos-pedidos)
     (group-by :usuario)
     (map quantia-de-pedidos-e-gasto-total-por-usuario)
     println)
