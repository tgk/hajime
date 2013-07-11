(ns hajime.models.execfile
  (:require [clojure.stacktrace :refer [root-cause]]
            [hajime.models.sandbox :refer [find-sb exec-file]]
            [noir.session :as session])
  (:import java.util.concurrent.TimeoutException))


(defn eval-file [fstr]
  (try
    (exec-file fstr (get (session/swap! find-sb) "sb"))
    (catch TimeoutException _
      {:error true :message "Execution Timed Out!"})
    (catch Exception e
      {:error true :message (str (root-cause e)) :line (. e line)})))