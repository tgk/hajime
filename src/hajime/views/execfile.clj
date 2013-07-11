(ns hajime.views.execfile
  (:require [noir.core :refer [defpage]]
            [hajime.models.execfile :refer [eval-file]]
            [noir.response :as resp]))

  
(defpage [:post "/execfile.json"] {:keys [expr jsonp]}
  (if expr
    (let [{:keys [error message line success] :as result} (eval-file expr)]
      (if jsonp
        (resp/jsonp jsonp result)
        (resp/json result)))
    (if jsonp
      (resp/jsonp jsonp {:error true :message "No expression sent!"} )
      (resp/json {:error true :message "No expression sent!"}))))
