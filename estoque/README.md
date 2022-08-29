# Estoque

Um projeto Clojure para estudo da linguagem.

## Tópicos

Para primeira execução deve-se clicar no arquivo "project.clj" e selecionar "Run 'REPL for estoque'",
onde REPL significa _read-eval-print loop_.

O **Namespace** é especificado pela macro `ns` na parte superior do arquivo e deve corresponder ao caminho do local do arquivo.
É um contexto  e um container para vars, lower-case e usa hífen(-) por padrão para separar palavras.
O comando utilizado para utilizar um namespace específico é `(use 'estoque.aula2)`.

**Funções** são "coisas", é a maneira informal de dizer que aqui em Clojure funções são tratadas como algo muito importante, tão importantes quanto dados.
Isto é, você pode trabalhar com símbolos que referenciam funções. Funções são "first class citizens".
Inclusive você pode passá-las como argumento para outras funções, ou recebê-las como retorno de funções.
Funções que recebem ou retornam funções são chamadas de "higher order functions".
Utiliza-se `%` para fazer uma função lambda.

Na documentação é possível acessar a descrição de diversas funções, [https://clojuredocs.org/clojure.core](https://clojuredocs.org/clojure.core).

## Intellij IDE
Adicionar o plugin "Cursive".
No menu "Code" encontra-se a opção "Reformat code".

Alguns atalhos:
 - Cmd + Shift + L carrega novo arquivo no REPL, reload.
 - Cmd + Shift + P, com o cursor localizado em um trecho de código, esse atalho executa o escopo em questão
 - Cmd + Shift + K adiciona para dentro do escopo algo à direita dos parêntesis.
 - Cmd + Shift + J remove do escopo algo para a direita dos parêntesis.


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
