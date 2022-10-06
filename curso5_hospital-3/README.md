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

Adicionamos ao projeto a biblioteca [Schema](https://github.com/plumatic/schema), uma dependência para descrição e validação de dados declarativos. É possível acessar mais informações, além da página do github, aqui: [https://plumatic.github.io/schema/schema.core.html](https://plumatic.github.io/schema/schema.core.html).

Um **schema** é uma estrutura de dados Clojure(Script) que descreve uma forma de dados, que pode ser usada para documentar e validar funções e dados.

#### [s/defn](https://plumatic.github.io/schema/schema.core.html#var-defn)
Define uma função assim como no Clojure, porém pode fornecer tipos de schema aos símbolos dos argumentos e ao retorno da função.
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
Indica que a partir daquele trecho será habilitada a checagem em tempo de execução. Variável global.
`(s/set-fn-validation! true)`

Parecido temos a macro "s/with-fn-validation" que habilita a checagem dentro de seu escopo.
`(s/with-fn-validation (foo 1 2))    ; ==> 2`

#### [s/validate](https://plumatic.github.io/schema/schema.core.html#var-validate)
Lança uma exceção se o valor não satisfizer o schema; caso contrário, retorna o valor.
Para validar muitos dados, é muito mais eficiente criar um 'validator' uma vez e chamá-lo em cada um deles.

````clojure
    (s/validate s/Num 42)      ;; 42
    (s/validate s/Num "42")    ;; RuntimeException: Value does not match schema: (not (instance java.lang.Number "42"))
    (s/validate [s/Num] nil)   ;; Válido - nil considerado vetor vazio
````

#### s/pred
Utilitário para construção de schemas.
```clojure
    (pprint (s/validate (s/pred odd?) 5))                       ; 5
    (pprint (s/validate (s/pred odd?) 2))                       ; Value does not match schema: (not (odd? 2)
```

#### [s/constrained](https://plumatic.github.io/schema/schema.core.html#var-constrained)
Utilitário para construção de schemas.
Um schema com uma pós-condição adicional. Difere de `conditional` com um único schema, em que o predicado é verificado *após* o principal esquema. Isso pode levar a melhores mensagens de erro.
```clojure
    (s/defschema OddLong (s/constrained long odd?))             ; valida se é long e ímpar
    (s/validate OddLong 1)                                      ; pass
    (s/validate OddLong "try")                                  ; Exception - Value does not match schema: (not (instance? java.lang.Long "try"))
    (s/validate OddLong 2)                                      ; Exception - Value does not match schema: (not (odd? 2))
```

#### [s/optional-key](https://github.com/plumatic/schema#map-schemas)

Define uma chave como opcional em um mapa. Se foo estiver presente deverá receber palavras chave.
```clojure
    (s/defschema FancyMap
      "If foo is present, it must map to a Keyword."
      {(s/optional-key :foo) s/Keyword})
    
    (s/validate FancyMap {:foo :a})             ; Ok
    (s/validate FancyMap {"a" "b"})             ; Exception - Value does not match schema: {"a" disallowed-key}
```
Keyworks por padrão determinam schemas obrigatórios, `required-key`.
```clojure
    (def Paciente
      {:id                          PosInt,
       :nome                        s/Str,
       :plano                       Plano })           ; se a chave é um keyword 0 ela é obrigatória
    
    (s/validate Paciente {:id 15, :nome "Guilherme"})  ; Exception pois não possui a keywork 'plano'
```

#### [s/maybe](https://plumatic.github.io/schema/schema.core.html#var-maybe)

Indica que um valor pode ser nulo ou satisfazer o schema: `(maybe schema)`

## [Certificado](/certificados/Certificado-clojure-5-alura.pdf)

<object data="/certificados/Certificado-clojure-5-alura.pdf" width="700px" height="700px">
    <embed src="/certificados/Certificado-clojure-5-alura.pdf">
        <p>This browser does not support PDFs. Please download the PDF to view it: <a href="/certificados/Certificado-clojure-5-alura.pdf">Download PDF</a>.</p>
    </embed>
</object>
