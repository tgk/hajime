(ns hajime.execfile_test
  (:use hajime.models.eval 
        hajime.models.execfile
        hajime.models.sandbox
        clojure.test)
  (:require noir.session))


(def sb (make-sandbox))

(deftest eval-form-test
  (let [form "(do (println 10) (+ 3 3))"
        result (eval-string form sb)]
    (is (= "10\n" (-> result :result first str)))
    (is (= "6" (-> result :result second str)))
    (is (= (read-string form) (-> result :expr)))))

(deftest eval-request-test
  (is (= "Execution Timed Out!" (:message (eval-request "(while true)")))))

(deftest eval-multi-str-test
  (let [file "(defn add33 [n] (+ 33 n)) (def a {:a 1 :b 2})"]
    (eval-multi-str file sb)
    (is (= 77 (-> (eval-string "(add33 44)" sb) :result second)))
    (is (= 1 (-> (eval-string "(:a a)" sb) :result second)))))
