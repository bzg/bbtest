#!/usr/bin/env bb

(ns bbtest)

(defn -main [& args]
  (println "Rien!"))

(when (= *file* (System/getProperty "babashka.file"))
  (println "Rien!"))


