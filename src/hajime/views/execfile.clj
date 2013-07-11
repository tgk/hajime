(ns hajime.views.execfile
  (:require [noir.core :refer [defpage]]
            [hajime.models.execfile :refer [eval-file]]
            [noir.response :as resp]))

(defpage "/execfile.json" {:keys [expr jsonp]}
  (let [{:keys [expr result error message] :as res} (eval-file expr)
        data (if error
               res
               (let [[out res] result]
                 {:expr (pr-str expr)
                  :result (str out (pr-str res))}))]
    
    (if jsonp
      (resp/jsonp jsonp data)
      (resp/json data))))
