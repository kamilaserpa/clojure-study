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

### [Juxt](https://clojuredocs.org/clojure.core/juxt)
Recebe um conjunto de funções e retorna um fn que é a justaposição desses fns.
O fn retornado recebe um número variável de argumentos e retorna um vetor contendo o resultado da aplicação de cada fn ao args (da esquerda para a direita).

```clojure
    ; Executa a fn ':a' em seguida a fn ':b' e retorna os valores em um vetor
    ((juxt :a :b) {:a 1 :b 2 :c 3 :d 4})    ;;=> [1 2]
```

### [Ref](https://clojuredocs.org/clojure.core/ref)
Enquanto as Vars garantem o uso seguro de locais de armazenamento mutáveis por meio do isolamento de thread, as referências transacionais (Refs) garantem o uso compartilhado seguro de locais de armazenamento mutáveis por meio de um sistema de memória transacional de software (STM).
As refs são vinculadas a um único local de armazenamento por toda a sua vida útil e só permitem que a mutação desse local ocorra dentro de uma transação (https://clojure.org/reference/refs).
```clojure
    ; create(ref)
    (def a (ref '(1 2 3)))
    
    ; read(deref)
    (deref a) ; -> (1 2 3)
    
    ; rewrite(ref-set)
    ; (ref-set a '(3 2 1)) err!
    (dosync (ref-set a '(3 2 1)))
    
    (deref a) ; -> (3 2 1)
```

### [Ref-set](https://clojuredocs.org/clojure.core/ref-set)

Deve ser chamado em uma transação. Define o valor de ref, retorna val: `(ref-set ref val)`.
```clojure
    (def foo (ref {}))

    (dosync
         (ref-set foo {:foo "bar"}))

    @foo
    ;{:foo "bar"}
```

### [Dosync](https://clojuredocs.org/clojure.core/dosync)
Executa o exprs (em um `do` implícito) em uma transação que engloba exprs e quaisquer chamadas aninhadas.
Inicia uma transação se nenhuma já estiver executando. Quaisquer efeitos em refs serão atômicos.
```clojure
    ; (dosync & exprs)
    ;; Create 2 bank accounts
    (def acc1 (ref 100))
    (def acc2 (ref 200))
    
    ;; How much money is there?
    (println @acc1 @acc2)
    ;; => 100 200
    
    ;; Either both accounts will be changed or none
    (defn transfer-money [a1 a2 amount]
      (dosync
        (alter a1 - amount)
        (alter a2 + amount)
        amount)) ; return amount from dosync block and function (just for fun)
    
    ;; Now transfer $20
    (transfer-money acc1 acc2 20)
    ;; => 20
    
    ;; Check account balances again
    (println @acc1 @acc2)
    ;; => 80 220
    
    ;; => We can see that transfer was successful
```

### [Alter](https://clojuredocs.org/clojure.core/alter)
Deve ser chamado em uma transação `(alter ref fun & args)`.
Define o valor-em-transacao de ref para `(apply fn valor-em-transacao args)` e retorna o valor-em-transacao do ref.

### [Ensure](https://clojuredocs.org/clojure.core/ensure)
Ensure somente garante que o valor não foi alterado durante a transação.
Imagina uma situação onde você precisa garantir que algo não foi alterado em um valor para que a transação ocorra, um ensure é a forma de garantir somente isso.

## [Certificado](/certificados/Certificado-clojure-3-alura.pdf)