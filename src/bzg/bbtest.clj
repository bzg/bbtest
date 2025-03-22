#!/usr/bin/env bb

(ns bzg.bbtest
  (:require [babashka.cli :as cli]
            [babashka.deps :as deps]
            [babashka.http-client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]))

;; Add the bblgum dependency
(deps/add-deps '{:deps {io.github.lispyclouds/bblgum {:git/sha "aa1e2e494a1441eb26c25830d21b4458259112c3"}}})

(require '[bblgum.core :as b])

(defn -main [& args]
  (println "Fetching FAQ data..."))

(when (= *file* (System/getProperty "babashka.file"))
  (-main))
