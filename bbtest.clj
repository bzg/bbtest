#!/usr/bin/env bb

(ns .bbtest
  (:require [babashka.cli :as cli]
            [babashka.deps :as deps]
            [babashka.http-client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]))

;; Add the bblgum dependency
(deps/add-deps '{:deps {io.github.lispyclouds/bblgum {:git/sha "aa1e2e494a1441eb26c25830d21b4458259112c3"}}})

(require '[bblgum.core :as b])

(defn fetch-faq []
  "Fetch FAQ data from code.gouv.fr"
  (println "Fetching FAQ from https://code.gouv.fr/data/faq.json")
  (let [response (http/get "https://code.gouv.fr/data/faq.json")]
    (when (= 200 (:status response))
      (json/parse-string (:body response) true))))

(defn extract-faq-items [faq-data]
  "Extract FAQ items from the data structure"
  (cond
    ;; If it's already a vector/list of maps
    (and (sequential? faq-data) (map? (first faq-data))) faq-data
    
    ;; If it's a map with a specific key containing the FAQ items
    (and (map? faq-data) (:faq faq-data)) (:faq faq-data)
    (and (map? faq-data) (:items faq-data)) (:items faq-data)
    
    ;; If it's a map with questions as keys
    (map? faq-data) 
    (mapv (fn [[k v]] 
           (if (map? v)
             (assoc v :question (name k))
             {:question (name k) :answer v}))
         faq-data)
    
    ;; Otherwise, just return it as a single item
    :else [{:question "FAQ" :answer (str faq-data)}]))

(defn normalize-faq-item [item]
  "Normalize different FAQ item structures to a consistent format"
  (cond
    ;; If it has question/answer keys
    (and (map? item) 
         (or (:question item) (:q item))
         (or (:answer item) (:a item)))
    {:question (str (or (:question item) (:q item)))
     :answer (str (or (:answer item) (:a item)))}
    
    ;; If it's a simple key-value pair
    (and (map? item) (= 1 (count item)))
    (let [[q a] (first item)]
      {:question (str q) :answer (str a)})
    
    ;; Default handling
    :else 
    {:question (str (pr-str item))
     :answer "Unknown format"}))

(defn display-faq-item [item]
  "Display a single FAQ item"
  (println)
  (println "Question:")
  (println (:question item))
  (println)
  (println "Answer:")
  (println (:answer item)))

(defn browse-faq [items]
  "Browse FAQ items with gum"
  (let [questions (mapv :question items)
        selected  (b/gum :table questions)]
    (when selected
      (let [selected-item (first (filter #(= (:question %) selected) items))]
        (display-faq-item selected-item)))))

(defn -main [& args]
  (println "Fetching FAQ data...")
  (if-let [faq-data (fetch-faq)]
    (let [items (mapv normalize-faq-item (extract-faq-items faq-data))]
      (println "Found" (count items) "FAQ items")
      (browse-faq items))
    (println "Failed to fetch FAQ data")))

(when (= *file* (System/getProperty "babashka.file"))
  (-main))
