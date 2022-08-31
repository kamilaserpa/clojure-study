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
