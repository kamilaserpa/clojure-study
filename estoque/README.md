# Clojure: programação funcional (curso 1)

Um projeto Clojure para estudo da linguagem em acompanhamento à Plataforma Alura.

## Tópicos

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
   
## Intellij IDE
Adicionar o plugin "Cursive".
No menu "Code" encontra-se a opção "Reformat code".

Alguns atalhos:
 - Cmd + Shift + L carrega novo arquivo no REPL, reload.
 - Cmd + Shift + P, com o cursor localizado em um trecho de código, esse atalho executa o escopo em questão
 - Cmd + Shift + K adiciona para dentro do escopo algo à direita dos parêntesis.
 - Cmd + Shift + J remove do escopo algo para a direita dos parêntesis.
 - Cmd + Alt + L identação


## License

Copyright © 2022 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
