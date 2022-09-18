(ns estoque.aula2)

(defn valor-descontado
  "Retorna o valor com desconto de 10% se o valor brurto for maior que 100."
  [valor-bruto]
  (if (> valor-bruto 100)
    (let [taxa-de-desconto (/ 10 100)
          desconto         (* valor-bruto taxa-de-desconto)]
      (println "Calculando desconto de " desconto)
      (- valor-bruto desconto))
    valor-bruto))

(println
  (valor-descontado 10))

(valor-descontado 150)
(valor-descontado 1000)
