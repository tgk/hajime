(ns hajime.views.eval
  (:require [noir.core :refer [defpage]]
            [hajime.models.eval :refer [eval-request]]
            [noir.response :as resp]))


(defpage [:post "/eval.json"] {:keys [expr jsonp]}
  (if expr
    (let [{:keys [expr result error message] :as res} (eval-request expr)
          data (if error
                 res
                 (let [[out res] result]
                   {:expr (pr-str expr)
                    :result (str out (pr-str res))}))]
    
      (if jsonp
        (resp/jsonp jsonp data)
        (resp/json data)))
     (if jsonp
        (resp/jsonp jsonp {:error true :message "No expression sent!"})
        (resp/json {:error true :message "No expression sent!"}))))
