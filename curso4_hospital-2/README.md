# Clojure: Recor, protocol e multi method (curso 4)

Um projeto Clojure para estudo da linguagem em acompanhamento à Plataforma Alura.

## Tópicos

### [Defrecord](https://clojuredocs.org/clojure.core/defrecord)

`(defrecord name [& fields] & opts+specs)`

Quando Clojure está rodando na JVM, Records são transformados em tempo de compilação para classes Java.
Dado `(defrecord TypeName ...)`, duas funções de fábrica serão definido: `->TypeName`, tomando parâmetros posicionais para os campos, e `map->TypeName`, levando um mapa de palavras-chave para valores de campo.

```clojure
  (defrecord Person [fname lname address])
  (defrecord Address [street city state zip])

  (def stu (Person. "Stu" "Halloway"
                  (Address. "200 N Mangum"
                            "Durham"
                            "NC"
                            27701)))

  (:lname stu) ; -> "Halloway"
```

### [Defprotocol](https://clojuredocs.org/clojure.core/defprotocol)
Um protocolo é um conjunto nomeado de métodos nomeados e suas assinaturas.
Possui funcionamento que lembra uma interface em java.

```clojure
    (defprotocol Fly
      "A simple protocol for flying"
      (fly [this] "Method to fly"))

    (defrecord Bird [name species]
      Fly
      (fly [this] (str (:name this) " flies...")))

    (extends? Fly Bird) ; -> true
```

### [Extend-type](https://clojuredocs.org/clojure.core/extend-type)

Estenda um [tipo] para implementar um ou mais [protocolos]. Pode ser compreendido como a implementação de uma interface java.

```clojure
    ;;; This is a library for the shopping result.
    (defrecord Banana [qty])
    
    ;;; 'subtotal' differ from each fruit.
    
    (defprotocol Fruit
      (subtotal [item]))
    
    (extend-type Banana
      Fruit
      (subtotal [item]
        (* 158 (:qty item))))
```

### [Defmulti](https://clojuredocs.org/clojure.core/defmulti)

`(defmulti name docstring? attr-map? dispatch-fn & options)` <br>
Cria um novo multimethod com a função 'dispatch-fn' associada, símbolos terminados com "?" são opcionais.
A função `dispatch-fn` pode ser utilizada e ao retornar um valor, este pode ser atribuído à `dispatch-val` do `defmethod`.
Desse modo podemos definir métodos, comportamentos distintos para cada valor retornado.

### [Defmethod](https://clojuredocs.org/clojure.core/defmethod)

`(defmethod multifn dispatch-val & fn-tail)` <br>
Cria e instala um novo multimethod asociado ao `dispatch-val`.
Veja um exemplo em [hospital-2/src/hospital_2/defmulti_example.clj](hospital-2/src/hospital_2/defmulti_example.clj).

```clojure
    (defmulti greeting
              (fn [x] (:language x))) ; a propriedade 'language' é passada como parâmetro para as implementações
    
    (defmethod greeting "English" [params]
      "Hello!")
    
    (defmethod greeting "French" [params]
      "Bonjour!")
    
    (defmethod greeting :default [params]        ; default handling
      (throw (IllegalArgumentException.
               (str "I don't know the " (:language params) " language"))))
    
    (println (greeting {:language "English"}))   ; "Hello!"
    (println (greeting {:language "French"}))    ; "Bonjour!"
    (println (greeting {:language "Spanish"}))   ; java.lang.IllegalArgumentException: I don't know the Spanish language
```

### [Cond](https://clojuredocs.org/clojure.core/cond)

`cond & clauses` Recebe um conjunto de pares de teste/expressões. Ele avalia cada teste um por um.
Se um teste retornar true, `cond` avalia e retorna o valor da expr correspondente e não avalia nenhuma das outros testes ou exprs seguintes.
```clojure
    (defn pos-neg-or-zero
      [n]
      (cond
        (< n 0) "negative"
        (> n 0) "positive"
        :else "zero"))
    
    (pos-neg-or-zero 5) ; "positive"
    (pos-neg-or-zero -1) ; "negative"
    (pos-neg-or-zero 0) ; "zero"
```
