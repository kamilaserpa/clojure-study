(ns hospital.aula4
  (:use [clojure pprint])
  (:require [hospital.logic :as h.logic]
            [hospital.model :as h.model]))

(println "----------------------------")

(defn chega-sem-malvadeza! [hospital pessoa]
  (swap! hospital h.logic/chega-em :espera pessoa)
  (println "> Após inserir" pessoa))

(defn simula-um-dia-em-paralelo
  "Simulação utilizando o mapv para forçar quase que imperativamente a execução do que seria lazy"
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]]
    ; map é lazy, não é iperativo
    (mapv
      #(.start (Thread. (fn [] (chega-sem-malvadeza! hospital %))))
      pessoas)

    (.start (Thread. (fn [] (Thread/sleep 4000)
                       (pprint hospital))))))

; (simula-um-dia-em-paralelo)

(println "----------------------------")

(defn simula-um-dia-em-paralelo
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]
        starta-thread-de-chegada #(.start (Thread. (fn [] (chega-sem-malvadeza! hospital %))))]

    (mapv starta-thread-de-chegada pessoas)

    (.start (Thread. (fn [] (Thread/sleep 4000)
                       (pprint hospital))))))

; (simula-um-dia-em-paralelo)

(println "----------------------------")

(defn starta-thread-de-chegada
  [hospital pessoa]
  (.start (Thread. (fn [] (chega-sem-malvadeza! hospital pessoa)))))

(defn preparadinho [hospital]
  (fn [pessoa] (starta-thread-de-chegada hospital pessoa)))

(defn simula-um-dia-em-paralelo
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]]

    (mapv (preparadinho hospital) pessoas)                  ; map só permite um parâmetro, necessitamos enviar dois (hospital e pessoa)
    ; inserimos função com um dos argumentos e passamos o outro

    (.start (Thread. (fn [] (Thread/sleep 4000)
                       (pprint hospital))))))

; (simula-um-dia-em-paralelo)

(println "----------------------------")

(defn starta-thread-de-chegada-version
  ([hospital]
   (fn [pessoa] (starta-thread-de-chegada hospital pessoa)))

  ([hospital pessoa]
   (.start (Thread. (fn [] (chega-sem-malvadeza! hospital pessoa))))))

(defn simula-um-dia-em-paralelo-com-mapv-extraida
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]]

    (mapv (starta-thread-de-chegada-version hospital) pessoas)

    (.start (Thread. (fn [] (Thread/sleep 4000)
                       (pprint hospital))))))

; (simula-um-dia-em-paralelo-com-mapv-extraida)

(println "----------------------------")

(defn starta-thread-de-chegada
  [hospital pessoa]
  (.start (Thread. (fn [] (chega-sem-malvadeza! hospital pessoa)))))

(defn simula-um-dia-em-paralelo-com-partial
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]
        starta (partial starta-thread-de-chegada hospital)]

    (mapv starta pessoas)

    (.start (Thread. (fn [] (Thread/sleep 4000)
                       (pprint hospital))))))

(simula-um-dia-em-paralelo-com-partial)
