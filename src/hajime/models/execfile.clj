(ns hajime.models.execfile
  (:require [clojail.testers :refer [secure-tester-without-def blanket]]
            [clojail.core :refer [sandbox]]
            [clojure.stacktrace :refer [root-cause]]
            [noir.session :as session])
  (:import java.io.StringWriter
	   java.util.concurrent.TimeoutException))

(defn exec-str [sandbox str]
  "returns a map: {:error 'description'} or {:success 'loaded file correctly'}"
  (sandbox `(load-string ~str))
;
; (try (load-string ~str) (catch clojure.lang.Compiler$CompilerException er (. er line)))
;
(defn execfile [] nil)
