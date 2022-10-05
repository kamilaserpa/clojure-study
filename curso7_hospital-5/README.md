# Curso 7: geradores e testes de propriedades

Um projeto Clojure para estudo da linguagem em acompanhamento à Plataforma Alura.

## Tópicos

### Generators
A biblioteca [test.check](https://github.com/clojure/test.check) possui uma introdução (Introduction) sobre gerar valores para os testes, os [Generators](https://clojure.github.io/test.check/clojure.test.check.generators.html).

A função [`gen/sample`](https://clojure.org/guides/test_check_beginner#_development_tools) recebe um gerador e retornar uma coleção de amostras desse gerador.

```clojure
  (gen/sample gen/boolean)              ; (true false true true true false true true false false)
  (gen/sample (gen/vector gen/int))     ; retorna vetores de inteiros
  (gen/sample (gen/vector gen/int) 10)  ; retorna 10 vetores de inteiros
  (gen/sample (gen/vector gen/small-integer 12) 5)    ; retorna 5 vetores de inteiros, com tamanho até 12 dígitos
  (gen/sample (gen/vector gen/small-integer 1 5) 10)   ; retorna 10 vetores de inteiros, com tamanho de 1 a 5
```

### [Defspec](https://clojure.org/guides/test_check_beginner#_defspec)

`defspec` é uma macro para escrever testes baseados em propriedades que são reconhecidos e executados por clojure.test. Define uma nova var de teste clojure.test que usa `quick-check` para verificar a propriedade.

Se chamado com argumentos, o primeiro argumento é o número de tentativas, opcionalmente seguido por argumentos de keyword.

### [Properties](https://clojure.org/guides/test_check_beginner#_properties)

Uma propriedade é um teste real, combina um gerador com uma função que você deseja testar e verifica se a função se comporta conforme o esperado, considerando os valores gerados.

As propriedades são criadas usando a macro `clojure.test.check.properties/for-all`.
No exemplo abaixo a propriedade gera um vetor e então chama a função que está sendo testada (sort) três vezes.

```clojure
    (require '[clojure.test.check :as tc])
    (require '[clojure.test.check.generators :as gen])
    (require '[clojure.test.check.properties :as prop])
    
    (def sort-idempotent-prop
      (prop/for-all [v (gen/vector gen/int)]
        (= (sort v) (sort (sort v)))))
```

