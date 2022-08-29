(ns estoque.aula5)

; A vírgula é opcional
(def estoque {"Mochila" 10, "Camiseta" 5})

(println ">" estoque)
(println "> Temos" (count estoque) "elementos")

(println "> Chaves são:" (keys estoque))
(println "> Valores são:" (vals estoque))
(def estoque {:mochila  10
              :camiseta 5})                                 ; Não são exibidos na ordem necessariamente

; Padrão para definir chave de um mapa é :text, um keyword
(println (assoc estoque :cadeira 3))
(println (assoc estoque :mochila 1))
(println ">" estoque)

(println (update estoque :mochila inc))

(defn tira-um
  [valor]
  (println "Tirando um de" valor)
  (- valor 1))

; Alterando valor do vetor "estoque"
(println estoque)
(println (update estoque :mochila tira-um))
(println (update estoque :mochila #(- % 3)))

; Removendo/Desassociando chave do vetor
(println (dissoc estoque :mochila))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def pedido {
             :mochila  {:quantidade 2, :preco 80}
             :camiseta {:quantidade 3, :preco 40}})

(println "\n\n\n")
(println pedido)

(def pedido (assoc pedido :chaveiro {:quantidade 1, :preco 10}))
(println pedido)

; Utilizando o mapa como função, capturando o item de chave 'mochila'
(println (pedido :mochila))                                 ; mais raro pois se não houver valor hpa exception
(println (get pedido :mochila))
(println (get pedido :cadeira))
(println (get pedido :cadeira {}))
(println (:mochila pedido))
(println (:mochila pedido {}))                              ; MAIS COMUM. Caso não haja dados retorna um mapa vazio por padrão

(println (:quantidade (:mochila pedido)))                   ; código mais difícil de ler

; UPDATE-IN
; Atualiza um valor dentro do vetor (pedido/mochila/quantidade
(println (update-in pedido [:mochila :quantidade] inc))

; THREADING FIRST
; encadeia a chamada da função pedido 'pega a mochila', agora 'pega a quantidade (da mochila)'
; Melhor legibilidade
(println (-> pedido
             :mochila
             :quantidade))

(-> pedido
    :mochila
    :quantidade
    println)                                                ; mesmo resultado da anterior