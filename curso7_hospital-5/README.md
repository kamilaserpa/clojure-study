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
#### [gen/sample](https://clojure.github.io/test.check/clojure.test.check.generators.html#var-sample)

```clojure
  (sample generator)
  (sample generator num-samples)
```

Gera vetores, recebendo como parâmetro uma geradora de valores. "Num-samples" é o número de valores gerados, por padrão são 10. A sequência começa com pequenos valores do gerador.

#### [gen/fmap](https://clojure.github.io/test.check/clojure.test.check.generators.html#var-fmap)
Retorna um gerador como `gen` mas com valores transformados por `f`: `(fmap f gen)`

```clojure
    (gen/sample (gen/fmap str gen/nat)) 
    ; ("0" "1" "0" "1" "4" "3" "6" "6" "4" "2") 
    ; transforma cada um dos numeros naturais gerados pelo gen/nat em string
```

#### [gen/elements](https://clojure.github.io/test.check/clojure.test.check.generators.html#var-elements)
Cria um gerador que escolhe aleatoriamente um elemento de `coll`: `(elements coll)`.

```clojure
    (gen/sample (gen/elements [:foo :bar :baz]))
    ; (:foo :baz :baz :bar :foo :foo :bar :bar :foo :bar)
    ; gera 10 'samples' com um dos elementos (foo, bar, baz) escolhidos aleatoriamente
```

#### [gen/tuple](https://clojure.github.io/test.check/clojure.test.check.generators.html#var-tuple)
Cria um gerador que retorna um vetor, cujos elementos são criados pelos geradores na posição respectiva. Os elementos individuais encolhem de acordo com seu gerador, mas o vetor nunca diminuirá em contagem.

```clojure
    (s/def t (gen/tuple gen/nat gen/boolean))
    (gen/sample t)
    ;; => ([0 false] [0 true] [2 false] [1 true] [0 false] [5 false] [5 false] [0 true] [0 true] [5 true])
    
    (gen/sample (gen/tuple gen/small-integer gen/boolean))
    ; ([0 true] [0 false] [0 false] [-2 true] [-1 true] [-2 true] 
    ; [5 true] [-2 false] [-1 false] [1 false])
```

#### [gen/not-empty](https://clojure.github.io/test.check/clojure.test.check.generators.html#var-not-empty)

Modifica um gerador para que não gere coleções vazias.
```clojure
    ;; generate a vector of booleans, but never the empty vector
    ; Gera 4 vetores preenchidos com 2 strings alfanuméricas
    (gen/sample (gen/not-empty (gen/vector gen/string-alphanumeric 2)) 4)
    ; => (["" ""] ["" ""] ["8i" "l2"] ["f" "Q"])
```

### [Prismatic/Schema Generators](https://cljdoc.org/d/prismatic/schema-generators/0.1.3/api/schema-generators.generators)

Schema Generators é uma biblioteca que fornece formas de geração automática de dados de teste a partir de schemas.

```clojure
    ; Amostra 3 itens do gerador
    (g/sample 3 Animal)
    ;; => ({:especie "", :idade 0}
    ;; {:especie "", :idade 0}
    ;; {:especie "T", :idade 1})
    

    ; Faz a amostragem de um único elemento de tamanho baixo a moderado.
    (g/generate Arvore)
    ;; => {:especie "l!uSc*<"}

    ; Preenche com os dados passados, validados pelo schema, e gera amostra
    (c/complete {:idade 6} Animal)
    ;; => {:especie "WgJ$Ssm`71", :idade 6}
    
    (c/complete {:morde true} Animal)
    ;; => #schema.utils.ErrorContainer{:error {:morde disallowed-key}}
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

### Property based testing

No post [In praise of property-based testing](https://increment.com/testing/in-praise-of-property-based-testing/), David Maciver cita testes de propriedade, em um contexto que envolve uma senha a ser criptografada. Há uma biblioteca que faz isto, por meio da criação de hashes. Então, são testados um valor, e as combinações de parâmetros são setadas, trabalhando-se com inteiros (integers) e um texto (text), que é a senha em si (password).

Feito o teste, ele descobre um bug. O teste implica na senha criptografada ser exatamente a mesma que de fato deveria ser criptografada, mas se testássemos isso, cairíamos naquela situação de duplicação de códigos, e então não descobriríamos nenhum bug. Se é um criptografador de senhas que possui ida e volta, é possível testar que, tendo ambos, precisaremos voltar para o mesmo lugar de início.

Isto é, não testamos se o nosso código de ida faz exatamente o que deveria fazer, e sim que, se fizermos a ida, e depois a volta, esta precisa chegar ao mesmo lugar. Estaremos, assim, testando uma propriedade deste algoritmo de criptografia. Ou, se tivermos a mesma senha várias vezes, ela precisará ser criptografada por um mesmo valor, o que seria uma outra abordagem, sem volta, apenas ida.

Não importa quantas vezes criptografemos a senha, o resultado tem que ser o mesmo, não importa se a criptografia estiver sendo feita em paralelo, se utilizamos mais ou menos memória, idem. Este é um exemplo de teste de propriedade de algoritmo. Não estamos testando o resultado, e sim que ele será o mesmo todas as vezes em que realizarmos o teste.

Outro bom exemplo dado pelo autor do post é o de formatação de datas, análogo ao exemplo citado da criptografia. Se utilizarmos uma data de padrão brasileiro, "18/09/1981", passarmos à formatação americana, "09/18/1981", quando retornarmos ao padrão brasileiro, a data precisa ser a mesma, caso contrário, trata-se de um bug. Trabalhamos com uma propriedade do código, e não o código em si.

 Seriam exemplos de propriedades da transferência de pacientes do hospital, o total de pessoas no hospital antes da transferência deve ser igual ao total de pessoas no hospital após a transferência de pessoas entre departamentos.
