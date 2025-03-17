#!/usr/bin/env bb

;; Copyright (c) Bastien Guerry
;; SPDX-License-Identifier: EPL-2.0
;; License-Filename: EPL-2.0.txt

(ns bbtest)

;; (deps/add-deps '{:deps {org.clojars.askonomm/ruuter {:mvn/version "1.3.4"}}})

(when (= *file* (System/getProperty "babashka.file"))
  (println "Rien!"))


