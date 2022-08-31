(ns loja.core)

;["daniela" "guilherme" "cristiano"]
;{"guilherme" 37, "paulo" 39}


(map println ["daniela" "guilherme" "cristiano" "lucia" "ana"])
(println (first ["daniela" "guilherme" "cristiano" "lucia" "ana"]))
(println (rest ["daniela" "guilherme" "cristiano" "lucia" "ana"])) ; resto exceto o primeiro indice
(println (rest [])) ; retorna sequência vazia
(println (next ["daniela" "guilherme" "cristiano" "lucia" "ana"])) ; próximo após o primeiro índice
(println (next [])) ; retorna nulo, usado pra identificar quando não há itens
(println (seq []))
(println (seq [1 2 3 4])) ; seq de um vetor retorna uma sequencia com os mesmos elementos
; [] deixa explícito que é um vetor
