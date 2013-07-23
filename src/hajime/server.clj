(ns hajime.server
  (:require [noir.server :as server]
            [ring.middleware.file :refer [wrap-file]]
            [ring.util.response :refer [file-response content-type]]
            [ring.util.codec :refer [url-decode]]
            [clojure.string :as string]))

(defn request-css? [req]
  (re-find #"\.css" (:uri req)))

(defn reply-css [req root-path] 
  (let [path (.substring (url-decode (:uri req)) 1)]
    (content-type 
      (file-response path {:root root-path :index-files? true})
      "text/css")))

(defn css-mime-mdlwr [app root-path]
  (fn [req]
    (if (request-css? req)
      (reply-css req root-path)
      (app req))))


(server/add-middleware wrap-file (System/getProperty "user.dir"))
(server/add-middleware css-mime-mdlwr (System/getProperty "user.dir"))
(server/load-views "src/hajime")

(defn to-port [s]
  (when-let [port s] (Long. port)))

(defn hajime [& [port]]
  (server/start
   (or (to-port port)
       (to-port (System/getenv "PORT")) ;; For deploying to Heroku
       8801)
   {:session-cookie-attrs {:max-age 12000}}))

(defn -main [& args] (hajime (first args)))
