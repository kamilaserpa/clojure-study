(ns estoque.aula3)

; PREDICATE
; If - retorna nil quando a condição para caso false não é definida e acionada
(defn aplica-desconto?
  [valor-bruto]
  (println "Chamando a função 'aplica-desconto? if'")
  (if (> valor-bruto 100)
    true))

; When - retorna nil quando não satisfaz a condição
(defn aplica-desconto?
  [valor-bruto]
  (println "Chamando a função 'aplica-desconto? when'")
  (when (> valor-bruto 100)
    true))

; Caso mais direto
(defn aplica-desconto?
  [valor-bruto]
  (println "Chamando a função 'aplica-desconto?' direta")
  (> valor-bruto 100))

(println (aplica-desconto? 1000))
(println (aplica-desconto? 100))


(defn valor-descontado
  "Retorna o valor com desconto de 10% se o valor brurto for maior que 100."
  [valor-bruto]
  (if (aplica-desconto? valor-bruto)
    (let [taxa-de-desconto (/ 10 100)
          desconto (* valor-bruto taxa-de-desconto)]
      (- valor-bruto desconto))
    valor-bruto))

(println (valor-descontado 1000))
(println (valor-descontado 100))


