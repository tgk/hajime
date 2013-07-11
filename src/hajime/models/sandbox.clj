(ns hajime.models.sandbox
  (:require [clojail.testers :refer [secure-tester-without-def blanket]]
            [clojail.core :refer [sandbox]]
            [clojure.stacktrace :refer [root-cause]]
            [noir.session :as session])
  (:import java.io.StringWriter
	   java.util.concurrent.TimeoutException))

(def try-clojure-tester
  (conj secure-tester-without-def (blanket "hajime" "noir")))

(defn make-sandbox []
  (sandbox [] ; this is a ridiculously open sandbox... 
           :timeout 2000
           :init '(do (require '[clojure.repl :refer [doc source]])
                      (future (Thread/sleep 10800000)
                              (-> *ns* .getName remove-ns)))))

(defn find-sb [old]
  (if-let [sb (get old "sb")]
    old
    (assoc old "sb" (make-sandbox))))

(defn eval-form [form sbox]
  (with-open [out (StringWriter.)]
    (let [result (sbox form {#'*out* out})]
      {:expr form
       :result [out result]})))

(defn eval-string [expr sbox]
  (let [form (binding [*read-eval* false] (read-string expr))]
    (eval-form form sbox)))

(defn exec-file [sandbox str]
  "returns a map: {:error 'description'} or {:success 'loaded file correctly'}"
  (sandbox `(load-string ~str)))

; (try 
;   (load-string ~str) 
;   (catch clojure.lang.Compiler$CompilerException er 
;     (. er line)))

