# Clojure: coleções no dia a dia (curso 2)

Um projeto Clojure para estudo da linguagem em acompanhamento à Plataforma Alura.

## Tópicos


### First

A macro [`first`](https://clojuredocs.org/clojure.core/first) retorna o primeiro item de uma coloeção.
```clojure
    (first '(:alpha :bravo :charlie))   ;;=> :alpha
    (first nil)                         ;;=> nil
    (first [])                          ;;=> nil
```

### Rest

A macro [`rest`](https://clojuredocs.org/clojure.core/rest) retorna uma sequência de itens após o primeiro, possivelmente vazia. Chama seq em seu argumento.
```clojure
    (rest [1 2 3 4 5])            ;;=> (2 3 4 5)
    (rest '())                    ;;=> ()
    (rest nil)                    ;;=> ()
```

### Next

A macro [`next`](https://clojuredocs.org/clojure.core/next) retorna uma sequência de itens após o primeiro. Retorna nil se não houver mais itens.
```clojure
    (next '(:alpha :bravo :charlie))     ;; (:bravo :charlie)
    (next [])                            ;; => nil
```

### Seq

A macro [`seq`](https://clojuredocs.org/clojure.core/seq) retorna uma sequência na coleção. Se a coleção for vazio, retorna zero.
```clojure
    (seq '(1))  ;;=> (1)
    (seq [1 2]) ;;=> (1 2)
    (seq "abc") ;;=> (\a \b \c)

    ;; Corner cases
    (seq nil)   ;;=> nil
    (seq '())   ;;=> nil
    (seq [])    ;;=> nil
    (seq "")    ;;=> nil
```

### Recur

A macro [`recur`](https://clojuredocs.org/clojure.core/recur) avalia as expressões em ordem, então, em paralelo, 
religa o retorno do ponto de recursão para os valores de entrada da expressão. 
Indica para o clojure que estamos fazendo uma recursão e só pode aparecer como retorno da função (cauda).
```clojure
    ; Exemplo para cáluclo de fatorial: n! = n . (n – 1). (n – 2). (n – 3) ... 2,1
    (def factorial
      (fn [n]
        (loop [contador n               ; contador inicializado com valor 'n'
               acumulador 1]            ; acumulador inicializado com valor 1
          (println "contador:" contador "| acumulador:" acumulador "| n:" n)
          (if (zero? contador)
            acumulador
            (recur (dec contador) (* acumulador contador))))))
    ; no loop contador tomará o valor (dec contador) // dec retorna um número a menos
    ; e o acumulador tomará o valor da multiplicação (* acumulador contador)
    
    (println (factorial 4))
```

Na recursão deve ser explicitado o critério de parada da repetição.

### Multi-aridade

A aridade é simplesmente o número de argumentos que uma função pode receber.
Uma maneira de definir [funções multiaridade](https://theburningmonk.com/2013/09/clojure-multi-arity-and-variadic-functions/)
é usar a macro defn, por exemplo:
```clojure
    (defn greet
      ([] (greet "you"))                ;; implementação com aridade 0
      ([name] (print "Hello" name)))    ;; implementação com aridade 1

    (greet)                             ;; => Hello you
    (greet "World")                     ;; => Hello World
```
