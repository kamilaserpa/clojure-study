(ns estoque.aula6)

(def pedido {
             :mochila  {:quantidade 2, :preco 80}
             :camiseta {:quantidade 3, :preco 40}})

; Não funciona
;(defn imprime-e-15 [chave valor]
; (println chave "e" valor)
; 15)

; Recebe o vetor e desestrutura em 'chave valor', no exemplo
(defn imprime-e-15 [[chave valor]]
  (println chave "<e>" valor)
  15)

(println (map imprime-e-15 pedido))

; Refatora map apenas com o valor
(defn imprime-e-15 [[chave valor]]
  valor)

(println (map imprime-e-15 pedido))

; Calculando o custo dos produtos
(defn preco-por-produto [[chave valor]]
  (* (:quantidade valor) (:preco valor)))

(println "Total por item:" (map preco-por-produto pedido))
(println "Total do pedido:" (reduce + (map preco-por-produto pedido)))

; Não usamos o parâmetro "chave", padrão substituuímos por underline "_"
(defn preco-por-produto [[_ valor]]
  (* (:quantidade valor) (:preco valor)))

(println "Total por item:" (map preco-por-produto pedido))
(println "Total do pedido:" (reduce + (map preco-por-produto pedido)))

; Definindo função para total
(defn total-do-pedido
  [pedido]
  (reduce + (map preco-por-produto pedido)))

(println "Total do pedido:" (total-do-pedido pedido))

; THREAD LAST
; passa primeiro a função (preco-por-produto), depois a coleção em map/reduce
(defn total-do-pedido
  [pedido]
  (->> pedido
       (map preco-por-produto)
       (reduce +)))

(println "Total do pedido:" (total-do-pedido pedido))

; CÓDIGO FINAL DA AULA
(defn preco-total-do-produto [produto]
  (* (:quantidade produto) (:preco produto)))

(defn total-do-pedido
  [pedido]
  (->> pedido
       vals
       (map preco-total-do-produto)
       (reduce +)))
(println "Total do pedido:" (total-do-pedido pedido))

;;;;;;;;

(def pedido {
             :mochila  {:quantidade 2, :preco 80}
             :camiseta {:quantidade 3, :preco 40}
             :chaveiro {:quantidade 1}})


(defn gratuito?
  [[chave item]]
  (<= (get item :preco 0) 0))

(println "Filtrando gratuitos")
(println (filter gratuito? pedido))

;
(defn gratuito?
  [item]
  (<= (get item :preco 0) 0))

(println "Filtrando gratuitos. Uma função anônima recebe chave e valor e repassa o valor/item para a função gratuito")
(println (filter (fn [[chave item]] (gratuito? item)) pedido))

(println "Filtrando gratuitos. Passando para a função 'gratuito?' apenas o segundo elemento (valor), ignorando a chave")
(println (filter #(gratuito? (second %)) pedido))

(defn pago?
  [item]
  (not (gratuito? item)))

(println (pago? {:preco 50}))
(println (pago? {:preco 0}))

; Comp retorna uma função, abaixo atribuímos a um símbolo
(def pago? (comp not gratuito?))
(println (pago? {:preco 50}))
(println (pago? {:preco 0}))
