# Clojure: Schemas (curso 5)

## Tópicos

### [Contains?](https://clojuredocs.org/clojure.core/contains_q)

`(contains? coll key)` <br>
Retorna true se a chave estiver presente na coleção fornecida, caso contrário retorna falso. Observe que para coleções indexadas numericamente como vetores e matrizes Java, isso testa se a chave numérica está dentro do gama de índices.

```clojure
    ;; `contains?` é objetivo com mapas
    (contains? {:a 1} :a)    ;=> true
    (contains? {:a nil} :a)  ;=> true
    (contains? {:a 1} :b)    ;=> false
    
    ;; Atenção para sequências, `contais?` é sobre índices e chaves, não sobre conteúdo 
    (contains? [:a :b :c] :b)  ;=> false , não existe o índice ':b'
    (contains? [:a :b :c] 2)   ;=> true
    (contains? ["a" "b"] "b")  ;=> false
    (contains? ["a" "b"] 1)    ;=> true , existe o índice 1
    (contains? "f" 0)          ;=> true
    (contains? "f" 1)          ;=> false , não existe o índice 1
    
    ;; `contains?` não deve ser usado para listas, pois não são sequências chaveadas
    (contains? '(1 2 3) 1)     ;; IllegalArgumentException (Clojure >=1.5)
    
    ;; Também funciona em native arrays, HashMaps or HashSets:
    (import '[java.util HashMap HashSet])
    (contains? (doto (HashSet.) (.add 1)) 1)        ;=> true
    (contains? (doto (HashMap.) (.put "a" 1)) "a")  ;=> true
    (contains? (int-array [1 2 3]) 0)               ;=> true
    (contains? (int-array [1 2 3]) 3)               ;=> false
    
    ;; Pode ser usado para testar associação em set (conjunto)
    (def s #{"a" "b" "c"})
    
    ;; Os membros de um conjunto são as chaves desses elementos.
    (contains? s "a")   ;=> true
    (contains? s "z")   ;=> false
```

### Schema

Adicionamos ao projeto a biblioteca [Schema](https://github.com/plumatic/schema), uma dependência para descrição e validação de dados declarativos. É possível acessar sua documentação aqui: [https://plumatic.github.io/schema/schema.core.html](https://plumatic.github.io/schema/schema.core.html).

#### [s/defn](https://plumatic.github.io/schema/schema.core.html#var-defn)
Define uma função assim como no Clojure, porém pode fornecer tipos de schema aos símbolos do argumento e do nome da função (para valor de retorno).
```clojure
    (s/set-fn-validation! true)
    
    (s/defn foo :- s/Num                     ; schema de retorno da fn: Num
      [x :- s/Int                            ; schema do símbolo x
       y :- s/Num]
      (* x y))

    (pprint (foo 2.5 4))                     ; Exception, 2.5 not integer
    (pprint (foo (foo 2 2.3) 1.5))           ; Exception, 2.6 not integer
```

#### s/fn-schema
Obtém o esquema de uma função:

`(println (s/fn-schema foo))         ; (=> Num Int Num)`

#### s/set-fn-validation
Indica que a partir daquele trecho será habilitada a checagem em tempo de execução.
`(s/set-fn-validation! true)`

Parecido temos a macro "s/with-fn-validation" que habilita a checagem dentro de seu escopo.
`(s/with-fn-validation (foo 1 2))    ; ==> 2`

