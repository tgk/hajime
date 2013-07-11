(ns hajime.models.execfile
  (:require [clojure.stacktrace :refer [root-cause]]
            [hajime.models.sandbox :refer [find-sb eval-multi-str]]
            [noir.session :as session])
  (:import java.util.concurrent.TimeoutException))


(defn eval-file [fstr]
  (try
    (eval-multi-str fstr (get (session/swap! find-sb) "sb"))
    (catch TimeoutException _
      {:error true :message "Execution Timed Out!"})
    (catch Exception e
      {:error true :message (str (root-cause e))})
    (catch clojure.lang.Compiler$CompilerException e 
      {:error true :message (str (root-cause e)) :line (. e line)})))
