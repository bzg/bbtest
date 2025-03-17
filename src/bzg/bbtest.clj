#!/usr/bin/env bb

(ns bzg.bbtest)

(defn -main [& args]
  (println "Rien!"))

(when (= *file* (System/getProperty "babashka.file"))
  (println "Rien!"))


