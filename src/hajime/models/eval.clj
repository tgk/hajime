(ns hajime.models.eval
  (:require [clojure.stacktrace :refer [root-cause print-stack-trace]]
            [hajime.models.sandbox :refer [find-sb eval-string]]
            [noir.session :as session])
  (:import java.util.concurrent.TimeoutException))

(defn eval-request [expr]
  (try
    (eval-string expr (get (session/swap! find-sb) "sb"))
    (catch TimeoutException _
      {:error true :message "Execution Timed Out!"})
    (catch Exception e
      (print-stack-trace e)
      {:error true :message (str (root-cause e))})))
