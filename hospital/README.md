# Clojure: mutabilidade com átomos e refs (curso 3)

Um projeto Clojure para estudo da linguagem em acompanhamento à Plataforma Alura.

## Tópicos

### [Peek](https://clojuredocs.org/clojure.core/peek)

Para uma lista ou fila, captura o primeiro elemento, o mesmo que o `first`. <br>
Para um vetor captura o último elemento, o mesmo que `last`, mas muito mais eficiente.
Se a coleção estiver vazia, retorna `nil`.

```clojure
      user=> (peek '(1 2 3 4)) ;;=> 1
      user=> (peek [1 2 3 4]) ;;=> 4
      user=> (peek []) ;;=> nil
```

### [Pop](https://clojuredocs.org/clojure.core/pop)

Para uma lista ou fila remove o primeiro item. <br>
Para um vetor remove o último item.
Se a coleção está vazia, lança uma exceção. Nota - não é o mesmo como próximo/último.
```clojure
      user=> (pop '(1 2 3 4)) ;;=> (2 3 4)
      user=> (pop [1 2 3 4]) ;;=> [1 2 3]
      user=> (pop []) ;;=> nil
      (pop ())        ;;IllegalStateException Can't pop empty list
```

### [Conj](https://clojuredocs.org/clojure.core/conj)

Retorna uma nova coleção com o xs 'adicionado'.
A adição pode acontecer em diferentes 'lugares' dependendo do tipo concreto.
```clojure
    (conj [1 2 3] 4) ;;=> [1 2 3 4] adiciona ao final do vetor
    (conj '(1 2 3) 4) ;;=> (4 1 2 3) adiciona ao início da lista
```

### [Ex-info](https://clojuredocs.org/clojure.core/ex-info)

Cria uma instância de ExceptionInfo, uma subclasse RuntimeException que carrega um mapa de dados adicionais.
```clojure
    (throw
      (ex-info "This exception is not caught" {:useless :data}))
```

### [Atom](https://clojuredocs.org/clojure.core/atom)

Cria e retorna um Atom com um valor inicial de x e zero ou mais opções (em qualquer ordem).
Se o mapa de metadados for fornecido, ele se tornará os metadados no átomo.

### [Swap!](https://clojuredocs.org/clojure.core/swap!)

Troca atomicamente o valor do átomo para ser: `(apply f current-value-of-atom args)`.
Note que `f` pode ser chamado várias vezes e, portanto, deve estar livre de efeitos colaterais.
O `swap!` deve ser utilizado com uma função que não apresenta problemas ao ser chamada novamente,
com threads concorrentes ele percebe quano o valor de uma variável é alterado e realiza uma nova chamada da função,
caracterizando `busy retry`, ou seja, constante tentativa.

Então, a vantagem do átomo é pararmos de ter trabalhos desnecessários com o programa de acesso de funções, valores, travas etc. 
para simplesmente chamar o `swap!` ou recurso equivalente com uma função pura que executa o mínimo possível 
para realizar a alteração.

Caso tenhamos a preocupação de usar uma operação `swap!`, basta lembrar da segurança de `retry`;
ou seja, se algo for alterado, o átomo tenta novamente a transação, invocando a função novamente
com novos valores dos argumentos e finalizando a execução com sucesso.

### [Locking](https://clojuredocs.org/clojure.core/locking)

Apesar da abordagem mais comum de Clojure ser o sistema de retry de transações com átomos, a linguagem disponibiliza uma forma de trabalhar com locking também como o uso de travas de monitoramento com https://clojuredocs.org/clojure.core/locking.

### [Deref](https://clojuredocs.org/clojure.core/deref)
Dereferenciar o átomo com deref para acessar a fila de espera dentro de um mapa.
Quando aplicado a um var, agent ou atom, retorna seu estado atual.

### [Partial](https://clojuredocs.org/clojure.core/partial)
Recebe uma função `f` e quantidade menor do que os argumentos normais para f, e retorna um `fn` que recebe um número variável de argumentos adicionais.
Quando chamada, a função retornada chama f com argumentos + argumentos adicionais.
```clojure
    (def subtract-from-hundred (partial - 100))
    (subtract-from-hundred 10)      ; same as (- 100 10)
    ; 90
    (subtract-from-hundred 10 20)   ; same as (- 100 10 20)
    ; 70
```

### [Doseq](https://clojuredocs.org/clojure.core/doseq)

Executa repetidamente o corpo (presumivelmente com efeitos colaterais) com ligações e filtragem conforme
fornecido por um "for". Retorna zero.
```clojure
    (doseq [x [-1 0 1] ; multiplicada cada x por cada y
            y [1  2 3]]
      (prn (* x y)))
        -1 ; x -1 y 1
        -2 ; x -1 y 2
        -3 ; x -1 y 3
        0  ; x 0  y 1
        0  ; x 0  y 2
        0  ; x 0  y 3
        1  ; x 1  y 1
        2  ; x 1  y 2
        3  ; x 1  y 3
        nil
```

### [Dotimes](https://clojuredocs.org/clojure.core/dotimes)
Executa repetidamente o corpo (presumivelmente aplica efeitos colaterais) com o nome ligado a inteiros de 0 a n-1.

```clojure
    user=> (dotimes [n 4] (println "n is" n))
    n is 0
    n is 1
    n is 2
    n is 3
    nil
```
