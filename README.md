# Clojure

Projeto de estudo em acompanhamento aos cursos da Formação Clojure da plataforma de ensino Alura.

## [Curso 1](estoque) - Clojure: programação funcional

Para primeira execução deve-se clicar no arquivo "project.clj" e selecionar "Run 'REPL for estoque'",
onde REPL significa _read-eval-print loop_.

O **Namespace** é especificado pela macro `ns` na parte superior do arquivo e deve corresponder ao caminho do local do arquivo.
É um contexto  e um container para vars, lower-case e usa hífen(-) por padrão para separar palavras.
O comando utilizado para utilizar um namespace específico é `(use 'estoque.aula2)`.

### Characters Clojure

 - Vírgula `,` é tratado como espaço em branco.
 - `_` usado como argumento de função indica que este arugmento não será utilizado.
 - [Listas](https://clojure.org/reference/data_structures#Lists) são coleções heterogêneas sequenciais, implementadas como uma lista encadeada.
   ```clojure 
        (1 "two" 3.0)  ; uma lista de três valores
   ```
 - [Vetores](https://clojure.org/reference/data_structures#Vectors) são coleções heterogêneas, sequenciais, indexadas. As funções aplicadas sobre eles retornan uma nova instância com valores alterados, mantendo a imutabilidade do vetor inicial.
   ```clojure 
        ["a" 13.7 :foo]              ; vetor de três valores
        (get ["a" 13.7 :foo] 1)      ; recuperação do valor no índice 1 de um vetor
        (count [1 2 3])              ; contagem dos valores, 3
        user=> (conj [1 2 3] 4 5 6)  ; adicionando elementos ao vetor
        [1 2 3 4 5 6]
   ```
 - [Mapas](https://clojure.org/reference/data_structures#Maps) são coleções heterogêneas espcificadas com chaves e valores.
   ```clojure 
        user=> {:a 1 :b 2}         ; mapa com duas combinações de chave/valor
        user=> (keys {:a 1 :b 2})  ; obtenção das chaves de um mapa
        (:a :b)
   ```
 - [Keyword](https://clojure.org/guides/weird_characters#_keyword)
      `:` é o indicador para uma palavra-chave, frequentemente usadas como chaves em mapas, possuem comportamento melhor do que strings.
       Também pode ser utilizada como função para procurar a si em um mapa.
   ```clojure 
        user=> (type :test)       ; verificação do tipo Keyword
        clojure.lang.Keyword
        user=> (keyword "test")   ; criação de uma keyword a partir de uma string
        :test
        
        user=> (def my-map {:one 1 :two 2})
        #'user/my-map
        user=> (:one my-map)      ; obtém o valor da key :one invocando como uma função
        1
   ```
 - Funções

    **Funções** são "coisas", é a maneira informal de dizer que, em Clojure, funções são tratadas como algo muito importante, tão importantes quanto dados.
Isto é, você pode trabalhar com símbolos que referenciam funções. Funções são "first class citizens".
Inclusive você pode passá-las como argumento para outras funções, ou recebê-las como retorno de funções.
Funções que recebem ou retornam funções são chamadas de "higher order functions".
   
   - Uma [função anônima](https://clojure.org/guides/weird_characters#_anonymous_function) pode ser iniciada por `fn`ou `#(`. Utiliza-se `%` para fazer uma função lambda:
   ```clojure
      ; função anônima que recebe um argumento e print
      (fn [line] (println line))
      ; função anônima de mesmo comportamento, porém mais curta
      #(println %)
   ```
   - Função [`map`](https://clojure.org/reference/other_functions)
   ```clojure
      user=> (map (fn [x] (+ 2 x)) [1 2 3])
      (3 4 5)
      user=> (map #(+ 2 %) [1 2 3])
      (3 4 5)
   ```
   - [Reduce](https://clojuredocs.org/clojure.core/reduce) `(reduce f coll) (reduce f val coll)` <br>
     `f` deve ser uma função de 2 argumentos. Se `val` não for fornecido,
     retorna o resultado da aplicação de `f` aos 2 primeiros itens em `coll`, então aplicando `f` a esse resultado e ao 3º item, etc. <br>
     Se `coll` não contiver itens, `f` também não deve aceitar argumentos, e reduce retorna o
     resultado de chamar `f` sem argumentos. <br>
     Se `coll` tiver apenas 1 item, ele é retornado e `f` não é chamado. <br>
     Se `val` for fornecido, retorna o resultado da aplicação de `f` para `val` e o primeiro item em `coll`, então
     aplicando `f` a esse resultado e ao 2º item, etc. <br>
     Se `coll` não contiver itens, retorna `val e `f` não é chamado.
    ```clojure
      (reduce + [1 2 3 4 5])  ;;=> 15
      (reduce + [])           ;;=> 0
      (reduce + [1])          ;;=> 1
      (reduce + [1 2])        ;;=> 3
      (reduce + 1 [])         ;;=> 1
      (reduce + 1 [2 3])      ;;=> 6
   ```
   - [Comp](https://clojuredocs.org/clojure.core/comp)
   Pega um conjunto de funções e retorna um fn que é a composição desses fns.
   O fn retornado recebe um número variável de argumentos, aplica o fns mais à direita aos argumentos,
   nas próximas fn (da direita para a esquerda) para o resultado, etc.
   ```clojure
      (def negative-quotient (comp - /))   ;; #'user/negative-quotient
      (negative-quotient 8 3)              ;;=> -8/3
   
      ((comp str +) 8 8 8)                 ;;=> "24"
   ```
   Na documentação é possível acessar a descrição de diversas funções, [https://clojuredocs.org/clojure.core](https://clojuredocs.org/clojure.core).

 - [**Thread**](https://clojure.org/guides/threading_macros)
   - Por convenção, as funções principais que operam em sequências esperam a sequência como seu último argumento. Da mesma forma, pipelines contendo **map**, **filter**, **remove**, **reduce**, **into**, etc geralmente chamam a macro ´->>` "thread last".
   ```clojure
   (defn calculate* []
      (->> (range 10)
      (filter odd? ,,,)
      (map #(* % %) ,,,)
      (reduce + ,,,)))
   ```
   - As funções principais que operam em estruturas de dados, por outro lado, esperam o valor em que trabalham como seu primeiro argumento. Estes incluem **assoc**, **update**, **dissoce getsuas** -invariantes. Os pipelines que transformam mapas usando essas funções geralmente exigem a macro `->` "thread first".
   ```clojure
       (defn transform* [person]
          (-> person
             (assoc ,,, :hair-color :gray)
             (update ,,, :age inc)))
   ```

## [Curso 2](loja) - Clojure: coleções no dia a dia

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

### Nth

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

   
## Intellij IDE
Adicionar o plugin "Cursive".
No menu "Code" encontra-se a opção "Reformat code".

Alguns atalhos:
 - Cmd + Shift + L carrega novo arquivo no REPL, reload.
 - Cmd + Shift + P, com o cursor localizado em um trecho de código, esse atalho executa o escopo em questão
 - Cmd + Shift + K adiciona para dentro do escopo algo à direita dos parêntesis.
 - Cmd + Shift + J remove do escopo algo para a direita dos parêntesis.
 - Cmd + Alt + L identação
