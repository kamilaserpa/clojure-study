(ns estoque.aula4)

(def precos [30 700 1000])

(println (precos 0))
(println (get precos 0))

;(println (precos 17))                                       ; IndexOutOfBoundsException
(println (get precos 17))                                   ; retorna null

(println "Valor padrão 0 caso o indice não exista" (get precos 17 0))
(println "Valor padrão 0, mas índice existe" (get precos 2 0))

; ADICIONANDO ITEM AO VETOR
(println (conj precos 5))                                   ; cria vetor novo, adiciona ao final o valor "5" e retorna esse novo vetor

(println precos)

; ATUALIZANDO VETOR / COLECAO
(println (+ 5 1))                                           ; soma 1 ao valor
(println (inc 5))                                           ; soma 1 ao valor
(println (update precos 0 inc))                             ; ao índice 0 da colação 'precos' aplica a função incrementa 1
(println (update precos 1 dec))                             ; ao índice 1 da coleção aplica a função decrementa 1

(defn soma-1
  [valor]
  (println "Estou somando 1 em" valor)
  (+ valor 1))

(println (update precos 0 soma-1))


(defn soma-3
  [valor]
  (println "Estou somando 30 em" valor)
  (+ valor 3))

(println (update precos 0 soma-3))

; CÓDIGO DA AULA ANTERIOR

(defn aplica-desconto?
  [valor-bruto]
  (println "Chamando a função 'aplica-desconto?'")
  (> valor-bruto 100))

(defn valor-descontado
  "Retorna o valor com desconto de 10% se o valor bruto for maior que 100."
  [valor-bruto]
  (if (aplica-desconto? valor-bruto)
    (let [taxa-de-desconto (/ 10 100)
          desconto (* valor-bruto taxa-de-desconto)]
      (- valor-bruto desconto))
    valor-bruto))

(println "Preços:" precos)

; Apliuca a fn "valor-descontado" para cada índice do vator preço
(println "map" (map valor-descontado precos))

(println "range" (range 10))
(println "filter números pares" (filter even? (range 10)))

(println "Filtra precos em que se aplica desconto" (filter aplica-desconto? precos))

; Chamando a função 'valor-descontado' para cada item do retorno do filtro
(println (map valor-descontado (filter aplica-desconto? precos)))