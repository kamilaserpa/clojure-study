(ns loja.aula2)

; Contagem dos elementos

; ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]

(defn conta
  [total-ate-agora elementos]
  (recur (inc total-ate-agora) (rest elementos)))

;;;;
(defn conta
  [total-ate-agora elementos]
  (println "Total momentaneo:" total-ate-agora "elementos:" elementos)
  (if (next elementos)
    (recur (inc total-ate-agora) (next elementos))
    (inc total-ate-agora)))

(println "Versão 1")
(println (conta 0 ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]))
(println (conta 0 []))                                      ; retorna 1, falha
; (println (next [1]))
; quando ainda há um elemento o next o remove, retorna nulo e para o loop ainda faltando a contagem de 1

;;;;
(defn conta
  [total-ate-agora elementos]
  (if (seq elementos)
    (recur (inc total-ate-agora) (next elementos))
    total-ate-agora))

(println "Versão 2")
(println "->" (conta 0 ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]))
(println "->" (conta 0 []))


;;;;
; Implementando função com mais de uma aridade possível,
; ou seja, possibilidade de diferentes números de parâmetros

(defn conta

  ([elementos]
   (conta 0 elementos))

  ([total-ate-agora elementos]
   (if (seq elementos)
     (recur (inc total-ate-agora) (next elementos))
     total-ate-agora)))

(println "Versão 3")
(println "->" (conta ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]))
(println "->" (conta []))
(println "->" (conta 0 [1 2 3]))                            ; a fn "conta" pode receber 1 ou 2 parâmetro