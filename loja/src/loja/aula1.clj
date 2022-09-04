(ns loja.aula1)

; UTILIZANDO RECURSÃO PARA EVENTO DE LOOP

(println "\n\n** MEU MAPA COM LOOP INFINITO **")

(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (funcao primeiro)
    (meu-mapa funcao (rest sequencia))))

;;;

(println "\n\n** MEU MAPA com parada no False **")
(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if primeiro
      (do
        (funcao primeiro)
        (meu-mapa funcao (rest sequencia))))))

(meu-mapa println ["daniela" false "cristiano" "lucia" "ana" "***"])

;;;

(println "\n\n** MEU MAPA COM parada no Nil (OK!) **")
(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if (not (nil? primeiro))
      (do
        (funcao primeiro)
        (meu-mapa funcao (rest sequencia))))))

(meu-mapa println ["daniela" "guilherme" "cristiano" "lucia" "ana" "***"])
(meu-mapa println ["daniela" false "cristiano" "lucia" "ana" "***"])
(meu-mapa println [])
(meu-mapa println nil)

; Estouro da pilha de memória em execução por recursividade (StackOverflowError)
; (meu-mapa println (range 10000))



(println "\n\n** MEU MAPA COM INDICAÇÃO DE LOOP PARA COMPILADOR **")
; TAIL RECURSION
(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if (not (nil? primeiro))
      (do
        (funcao primeiro)
        (recur funcao (rest sequencia))))))

(meu-mapa println (range 10000))                            ; Execução sem estouro da pilha de execução

;;; Outro exemplo de função recursiva
(def factorial
  (fn [n]
    (loop [contador n acumulador 1]
      (println "contador:" contador "| acumulador:" acumulador "| n:" n)
      (if (zero? contador)
        acumulador
        (recur (dec contador) (* acumulador contador))))))
; in loop cnt will take the value (dec cnt)
; and acc will take the value (* acc cnt)

(println (factorial 4))

