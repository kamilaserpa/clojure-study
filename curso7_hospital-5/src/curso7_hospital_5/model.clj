(ns curso7-hospital-5.model
  (:require [schema.core :as s]))

(def fila-vazia clojure.lang.PersistentQueue/EMPTY)

(defn novo-hospital []
  {:espera       fila-vazia
   :laboratorio1 fila-vazia
   :laboratorio2 fila-vazia
   :laboratorio3 fila-vazia})

(defn novo-departamento []
  fila-vazia)

(s/def PacienteID s/Str)

(s/def Departamento (s/queue PacienteID))

(s/def Hospital {s/Keyword Departamento})

