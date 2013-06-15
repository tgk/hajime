(ns hajime.views.tutorial
  (:require [noir.core :refer [defpage]]))

(defpage [:post "/tutorial"] {page :page}
  (slurp (str "resources/public/tutorial/" page ".html")))
