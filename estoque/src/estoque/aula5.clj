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