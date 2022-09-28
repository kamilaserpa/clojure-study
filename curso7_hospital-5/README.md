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
