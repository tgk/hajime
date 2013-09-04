(ns hajime.views
  [:require [noir.core :refer [defpage]]
            [hajime.models :refer [eval-file eval-request]]
            [noir.response :as resp]]
  [:use [clostache.parser]])


(defpage "/" []
  (render (slurp "templates/home.html") {} ))

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

(def teacher-file (atom ""))

(defpage [:post "/execfile.json"] {:keys [expr jsonp teacher]}
  (if expr
    (let [{:keys [error message line success] :as result} (eval-file expr)]
      (when (= teacher "true")
        (reset! teacher-file expr))
      (if jsonp
        (resp/jsonp jsonp result)
        (resp/json result)))
    (if jsonp
      (resp/jsonp jsonp {:error true :message "No expression sent!"} )
      (resp/json {:error true :message "No expression sent!"}))))

(defpage [:get "/teacherfile.json"] {:keys [jsonp]}
  (if jsonp
    (resp/jsonp jsonp @teacher-file)
    (resp/json @teacher-file)))
