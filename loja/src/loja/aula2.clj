(ns loja.aula2)

; Contagem dos elementos

#_
(defn conta
  [total-ate-agora elementos]
  (recur (inc total-ate-agora) (rest elementos)))

;;;;
#_
(defn conta
  [total-ate-agora elementos]
  (println "Total momentaneo:" total-ate-agora "elementos:" elementos)
  (if (next elementos)
    (recur (inc total-ate-agora) (next elementos))
    (inc total-ate-agora)))

#_ #_ #_
(println "Versão 1")
(println (conta 0 ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]))
(println (conta 0 []))                                      ; retorna 1, falha
; (println (next [1]))
; quando ainda há um elemento o next o remove, retorna nulo e para o loop ainda faltando a contagem de 1

;;;;
#_
(defn conta
  [total-ate-agora elementos]
  (if (seq elementos)
    (recur (inc total-ate-agora) (next elementos))
    total-ate-agora))
#_ #_ #_
(println "Versão 2")
(println "->" (conta 0 ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]))
(println "->" (conta 0 []))


;;;;
; Implementando função com mais de uma aridade possível,
; ou seja, possibilidade de diferentes números de parâmetros
#_
(defn conta

  ([elementos]
   (conta 0 elementos))

  ([total-ate-agora elementos]
   (if (seq elementos)
     (recur (inc total-ate-agora) (next elementos))
     total-ate-agora)))

#_ #_ #_ #_
(println "Versão 3")
(println "->" (conta ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]))
(println "->" (conta []))
(println "->" (conta 0 [1 2 3]))                            ; a fn "conta" pode receber 1 ou 2 parâmetro

;;;;
; for total-ate-agora 0, elementos-restantes elementos
; enquanto tem elemento soma 1 no total-ate-agora
; envia o next para redefinir os elementos restantes

; A recursão é realizada em trecho de código dentro da função e não recursão da função em si.
; Este caso geralmente ocorre com trecho de código antes do loop.
; Nesse caso provavelmente a função está mais complexa do que deveria e pode ser dividida em duas

(defn conta
  [elementos]
  (println "Antes do loop (executado apenas uma vez)")
  (loop [total-ate-agora 0
         elementos-restantes elementos]
    (if (seq elementos-restantes)                           ; se tem elementos na coleção
      (recur (inc total-ate-agora) (next elementos-restantes)) ; soma 1 no total-ate-agora e atribui os elementos restantes ao loop
      total-ate-agora)))

(println "Versão 4")
(println "->" (conta ["daniela" "guilherme" "carlos" "paulo" "lucia" "ana"]))
(println "->" (conta []))
(println "->" (conta [1 2 3]))
