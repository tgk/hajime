(ns hajime.models
  [:require [clojure.stacktrace :refer [root-cause print-stack-trace]]
            [clojail.testers :refer [secure-tester-without-def blanket]]
            [clojail.core :refer [sandbox]]
            [noir.session :as session]]
  [:import java.io.StringWriter java.util.concurrent.TimeoutException])

(def try-clojure-tester
  (conj secure-tester-without-def (blanket "hajime" "noir")))

(defn make-sandbox []
  (sandbox [] ; this is a ridiculously open sandbox... 
           :timeout 3000
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

(defn eval-multi-str [str sandbox]
  "returns a map: {:error 'description'} or {:success 'loaded file correctly'}"
  (try
    (sandbox `(load-string ~str))
    (catch Exception e 
      (throw e)))
  {:success 1})


(defn eval-request [expr]
  (try
    (eval-string expr (get (session/swap! find-sb) "sb"))
    (catch TimeoutException _
      {:error true :message "Execution Timed Out!"})
    (catch Exception e
      (print-stack-trace e)
      {:error true :message (str (root-cause e))})))

(defn eval-file [fstr]
  (try
    (eval-multi-str fstr (get (session/swap! find-sb) "sb"))
    (catch TimeoutException _
      {:error true :message "Execution Timed Out!"})
    (catch Exception e
      {:error true :message (str (root-cause e))})
    (catch clojure.lang.Compiler$CompilerException e 
      {:error true :message (str (root-cause e)) :line (. e line)})))
