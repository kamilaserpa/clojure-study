(ns hospital_2.defmulti-example)

;this example illustrates that the dispatch type
;does not have to be a symbol, but can be anything (in this case, it's a string)

(defmulti greeting
          (fn [x] (get x "language")))

;params is not used, so we could have used [_]
(defmethod greeting "English" [params]
  "Hello!")

(defmethod greeting "French" [_]
  "Bonjour!")

;;default handling
(defmethod greeting :default [params]
  (throw (IllegalArgumentException.
           (str "I don't know the " (get params "language") " language"))))

;then can use this like this:
(def english-map {"id" "1", "language" "English"})
(def french-map {"id" "2", "language" "French"})
(def spanish-map {"id" "3", "language" "Spanish"})

(println (greeting english-map))
; "Hello!"
(println (greeting french-map))
; "Bonjour!"
(println (greeting spanish-map))
; java.lang.IllegalArgumentException: I don't know the Spanish language
