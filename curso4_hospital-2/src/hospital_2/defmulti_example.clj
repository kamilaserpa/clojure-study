(ns hospital_2.defmulti-example)

;this example illustrates that the dispatch type
;does not have to be a symbol, but can be anything (in this case, it's a string)

(defmulti greeting
          (fn [x] (:language x))) ; a propriedade 'language' é passada como parÂmetro para as implementações

;params is not used, so we could have used [_]
(defmethod greeting "English" [params]
  "Hello!")

(defmethod greeting "French" [params]
  "Bonjour!")

;;default handling
(defmethod greeting :default [params]
  (throw (IllegalArgumentException.
           (str "I don't know the " (:language params) " language"))))

(println (greeting {:language "English"}))
; "Hello!"
(println (greeting {:language "French"}))
; "Bonjour!"
(println (greeting {:language "Spanish"}))
; java.lang.IllegalArgumentException: I don't know the Spanish language
