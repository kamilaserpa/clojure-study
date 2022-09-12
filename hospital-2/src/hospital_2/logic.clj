(ns hospital_2.logic
  (:require [hospital_2.model :as h.model]))

(defn agora []
  (h.model/to-ms (java.util.Date.)))
