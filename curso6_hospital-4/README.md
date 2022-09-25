# Clojure: explorando testes (curso 6)

## Tópicos

### [Is](https://clojuredocs.org/clojure.test/is)
Função de asserrção, afirmação mais simples. Caso verdadeiro a assertividade passa, caso contrário verifica falha.
```clojure
    (use '[clojure.test :only [is]])

    (is (= 4 (+ 2 2)) "Two plus two should be 4") ; 'msg' é uma mensagem opcional para anexar à declaração
    (is (true? true))    ; true
    
    ;; false assertions print a message and evaluate to false
    (is (true? false))
    ;FAIL in clojure.lang.PersistentList$EmptyList@1 (NO_SOURCE_FILE:1)
    ;expected: (true? false)
    ;actual: (not (true? false))
    ;false
```

#### Formas especiais

`(is (thrown? ex body))` verifica se uma instância de `ex` é lançada a partir da execução do `body`, falha se não for lançada; em seguida, retorna a coisa lançada.

`(is (thrown-with-msg? ex re body))` verifica se uma instância de `ex` é
lançada E que a mensagem na exceção corresponde com a expressão regular `re`.

```clojure
    ; Verifica se a exceção específica é lançada a partir do body (/ 1 0) 
    (is (thrown? ArithmeticException (/ 1 0)))
    ; #<ArithmeticException java.lang.ArithmeticException: Divide by zero>

    ; Verifica a exceção lançada e a mensagem enviada, iniciando por "#"
    (is (thrown-with-msg? ArithmeticException #"Divide by zero" (/ 1 0)))
    ; #<ArithmeticException java.lang.ArithmeticException: Divide by zero>
```

### [Deftest](https://clojuredocs.org/clojure.test/deftest)

Define uma função de teste sem argumentos. É uma coleção de afirmações, [com ou sem expressões `testing`](https://practical.li/clojure/testing/unit-testing/writing-unit-tests.html). As funções de teste podem chamar outros testes, para que os testes possam ser compostos.

```clojure
    ;successful test example
    (ns testing)
    (use 'clojure.test)
        
    (deftest addition
             (is (= 4 (+ 2 2)))
             (is (= 7 (+ 3 4))))        ; => #'testing/addition
    
    (deftest subtraction
             (is (= 1 (- 4 3)))
             (is (= 3 (- 7 4))))        ; => #'testing/subtraction
    
    ;composing tests
    (deftest arithmetic
             (addition)
             (subtraction))
```

### [Testing](https://clojuredocs.org/clojure.test/testing)

[É uma macro para um conjunto de asserções (afirmações)](https://practical.li/clojure/testing/unit-testing/writing-unit-tests.html). Adiciona uma nova string à lista de contextos de teste. Pode ser aninhado, mas deve ocorrer dentro de uma função de teste (deftest).
```clojure
    (deftest alternate-use
             (testing "test a vector of `is`"
                      [(is true)
                       (is true)
                       (is true)]))
```

### [some->](https://clojuredocs.org/clojure.core/some-%3E)

Em thread first quando 'nil' é retornado por qualquer etapa, as etapas adicionais não são executadas e retorna 'nil'.
```clojure
    (some-> val
            step1
            step2
            step3)
```

### [When-let](https://clojuredocs.org/clojure.core/when-let)

No exemplo abaixo, se 'test' não for falso nem nulo, test é vinculado à propriedade 'name' e `do-something-with-name` é executado.

```clojure
    (when-let [name test]
      (do-something-with-name))
```

### [Defn-](https://clojuredocs.org/clojure.core/defn-)

O mesmo que defn, produzindo def **não** público.
```clojure
    (ns hospital-4.exercises)
  
    (defn- foo []
      "World!")
    
    (defn bar []
      (str "Hello " (foo)))
    
    (println (foo))                                 ; "World!"
    (println (bar))                                 ; "Hello World!"
    
    ; Another namespace
    (ns hospital-4.exercises-test
      (:require [hospital-4.exercises :as exercises]))
    
    (println (exercises/bar))                       ; "Hello World!"
    
    (println (exercises/foo))                       ; IllegalStateException, var: #'hospital-4.exercises/foo is not public
```