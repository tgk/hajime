(ns hajime.views.home
  [:require [noir.core :refer [defpartial defpage]]
            [hiccup.element :refer [javascript-tag link-to unordered-list]]
            [hiccup.page :refer [include-css include-js html5]]]
  [:use [clostache.parser]])


(defpage "/" []
  (render (slurp "templates/home.html") {} ))

