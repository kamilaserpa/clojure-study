(ns hospital-4.logic-test
  (:require [clojure.test :refer :all]
            [hospital-4.logic :refer :all]))                ; :referir pelo nome, sem prefixo, tudo do clojure.test

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

  (testing "Que cabe na fila quando tem pouco menos do que uma fial cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4]}, :espera))))

  (testing "Que cabe na fila quando tem pouca gente na fila"
    (is (not (cabe-na-fila? {:espera [1 2]}, :espera)))))
