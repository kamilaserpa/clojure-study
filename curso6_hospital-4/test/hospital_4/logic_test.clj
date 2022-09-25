(ns hospital-4.logic-test
  (:require [clojure.test :refer :all]
            [hospital-4.logic :refer :all])
  (:import (clojure.lang ExceptionInfo)))                   ; :referir pelo nome, sem prefixo, tudo do clojure.test

(deftest cabe-na-fila?-test                                 ; define símbolo

  ; BOUNDARY TESTES
  ; Exatamente na borda e one off, -1, +1, <=, >=, =

  ; Borda do zero
  (testing "Que cabe na fila"
    (is (cabe-na-fila? {:espera []} :espera)))

  ; Borda do limite
  (testing "Que não cabe na fila quando a fila está cheio"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5]}, :espera))))

  ; one off da borda do limite pra cima
  (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]}, :espera))))

  (testing "Que cabe na fila quando tem pouco menos do que uma fila cheia"
    (is (cabe-na-fila? {:espera [1 2 3 4]}, :espera)))

  (testing "Que cabe na fila quando tem pouca gente na fila"
    (is (cabe-na-fila? {:espera [1 2]}, :espera)))

  (testing "Que não cabe quando departamento não existe"
    ; hospital possui departamento 'espera' apenas
    (is (not (cabe-na-fila? {:espera [1 2 3 4]}, :raio-x)))))

(deftest chega-em-test

  (let [hospital-cheio {:espera [1 35 42 64 21]}]

    (testing "aceita pessoas enquanto cabem pessoas na fila"
      ; Implementação ruim, pois testa que escrevemos o que escrevemos,
      ; ou seja o código no corpo é o mesmo do código de teste, que é um teste inútil
      #_(is (= (update {:espera [1, 2, 3, 4]} :espera conj 5)
               (chega-em {:espera [1, 2, 3, 4]}, :espera, 5)))

      #_(is (= {:espera [1, 2, 3, 4, 5]}
               (chega-em {:espera [1, 2, 3, 4]}, :espera, 5)))

      ; FAZER TESTES NÃO SEQUENCIAIS!
      #_(is (= {:espera [1, 2, 5]}
               (chega-em {:espera [1, 2]}, :espera, 5)))

      ;------------------------------
      (is (= {:hospital {:espera [1 2 3 4 5]}, :resultado :sucesso}
             (chega-em {:espera [1, 2, 3, 4]}, :espera, 5)))


      (is (= {:hospital {:espera [1 2 5]}, :resultado :sucesso}
             (chega-em {:espera [1, 2]}, :espera, 5))))

    (testing "não aceita quando a fila está cheia"

      ; Verificando que uma Exception foi lançada.
      ; Código clássico horrível com exception genérica, pois qualquer outro erro genérico lança essa mesma exception
      ; e nosso teste passaria e não verificaria.
      #_(is (thrown? ExceptionInfo
                     (chega-em hospital-cheio, :espera 76)))

      ; Mesmo escolhendo uma exception do gênero, é perigoso,
      ; pois algum caso inesperado pode resultar nesta mesma exception
      #_(is (thrown? IllegalArgumentException
                     (chega-em hospital-cheio, :espera 76)))

      ; A mensagem em texto pode ser alterada sem dificuldade, abordagem possível, porém não a melhor
      #_(is (thrown-with-msg? ExceptionInfo #"Não cabe ninguém neste departamento."
                              (chega-em hospital-cheio, :espera 76)))

      ; Abordagem possível, porém essa função deveria retornar um hospital para
      ; a atualização de um átomo via swap, ao retornar nulo essa funcionalidade é perdida,
      ; comprometendo e quebrando nossa lógica.
      ; (is (nil? (chega-em hospital-cheio, :espera 76)))

      ; Outra maneira de testar
      ; Verifica o tipo de dados retornados na Exception para capturar o erro
      ; Vantagem de ser menos sensível que verificação da msg de erro
      ; Validação trabalhosa
      #_(is (try
              (chega-em hospital-cheio, :espera, 76)
              false                                         ; se deixar inserir pessoas na fila RN falha, não deveria chegar nesta linha
              (catch ExceptionInfo e
                (= :impossivel-colocar-pessoa-na-fila (:tipo (ex-data e))))))

      (is (= {:hospital hospital-cheio, :resultado :impossivel-colocar-na-fila}
             (chega-em hospital-cheio, :espera 76))))))




