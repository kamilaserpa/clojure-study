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
### Loop

No [`loop`](https://clojuredocs.org/clojure.core/loop) a recursão é realizada em trecho de código dentro da função e não de toda a função em si.
Este caso geralmente ocorre com trecho de código antes do loop.
Nesse caso provavelmente há um controle de fluxo que deveria ter sido isolado em outro trecho de código.

### For
Clojure possui também um [`for`](https://clojuredocs.org/clojure.core/for) que recebe uma sequência de expressões de inicialização e o corpo a ser executado.
Ele funciona de forma similar ao loop, iterando pelos elementos da sequência, mas permitindo já loopar e definir novos símbolos com :when, :while e :let.
Como comentamos no caso do loop, tradicionalmente você verá outras formas de executar um mesmo bloco ao invés de loop e for.

```clojure
    (println "For imprimirá os resultados:")
    (println " 1 * 1 = 1 | 1 * 2 = 2 | 1 * 3 = 3")
    (println " 2 * 1 = 2 | 2 * 2 = 4 | 2 * 3 = 6")
    (println " 3 * 1 = 3 | 3 * 2 = 6 | 3 * 3 = 9")

    (def digits [1 2 3])

  (println (for [x1 digits
                 x2 digits]
             (* x1 x2)))
```

### Importação de classe
Importação de classe `loja.db` em `loja.aula3`, utilizando `:as` para abreviação.
```clojure
    (ns loja.aula3
      (:require [loja.db :as l.db]))
````

### NTH

Retorna o valor no índice. Obtém retornos nulos se o índice estiver fora limites, [`nth`](https://clojuredocs.org/clojure.core/nth) lança uma exceção, a menos que não encontrado seja fornecido.
`NTH`(enésimo) também funciona para strings, arrays Java, regex Matchers e Lists e, em tempo O(n), para sequências.
```clojure
  ; (nth coll index)(nth coll index not-found)
  ; Note that nth uses zero-based indexing, so that
  ;   (first my-seq) <=> (nth my-seq 0)
  (def my-seq ["a" "b" "c" "d"])
  (nth my-seq 0)               ; => "a"
  (nth [] 0)                   ; => IndexOutOfBoundsException ...
  (nth [] 0 "nothing found")   ; => "nothing found"
```

### Take
Retorna uma sequência lenta dos primeiros n itens em coll, ou todos os itens se há menos de n.
Retorna um [transdutor](https://clojure.org/reference/transducers) com estado quando nenhuma coleta é fornecida.

```clojure
    ; (take n)(take n coll)
    (take 3 '(1 2 3 4 5 6))     ;;=> (1 2 3)
    (take 3 [1 2 3 4 5 6])      ;;=> (1 2 3)
    ;; returns all items if there are fewer than n
    (take 3 [1 2])              ;;=> (1 2)
    (take 1 [])                 ;;=> ()
    (take 1 nil)                ;;=> ()
```

### Some
Retorna o primeiro valor `true` de uma função (pred x) para qualquer x em col, senão retorna `nil`.
Um idioma comum do [`some`](https://clojuredocs.org/clojure.core/some) é usar um conjunto como pred.
por exemplo isso retornará :fred se :fred estiver na sequência, caso contrário nil: (algum #{:fred} col)

```clojure
    ;; 2 é par, então `some` para, 3 e 4 nunca serão testados
    (some even? '(1 2 3 4))      ;;=> true
    ;; they are all odd, so not true, i.e. nil
    (some even? '(1 3 5 7))      ;;=> nil
```

### Lazy e Eager

Existem consequências para cada tipo de função lazy,eager ou uma mescla dos dois.
Se o que será processado é infinito, não poderemos utilizar o processo eager, afinal não haverá memória o suficiente.
No caso de funções com efeito colateral, não é interessante mesclarmos lazy e eager, afinal isso demandaria um controle fino que pode ser trabalhoso.
Para grupos grandes e finitos, é interessante utilizarmos o processo lazy.
Precisamos sempre pensar se faz sentido otimizar o programa por essa via, e muitas vezes não será, pois nem sempre trabalhamos com vetores e listas de um milhão de elementos, por exemplo.

## [Certificado](https://cursos.alura.com.br/certificate/kamila-serpa/clojure-colecoes-no-dia-a-dia)
