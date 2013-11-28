(ns clj-dimension.process-data
  "Deal with the usual mess of data"
  (:use [clojure.java.io :as io])
  (:import [java.io BufferedReader StringReader]))

(defn handle-data-file
  [a-data-file & options]
  
  (let [lines (-> a-data-file
                  io/reader
                  line-seq)]
    (if (some #{:ignore-first}
              (set options))
      (rest lines) lines)))

(defn open-prices-only  
  "Produce a NASDAQ dataset with only opening prices"
  [nasdaq-data-path]
  (let [only-prices (fn [data-files]
                      (filter
                       (fn [a-data-file]
                         (re-find #"daily_prices"
                                  (.getAbsolutePath
                                   a-data-file)))
                       data-files))
        
        data-files  (->> nasdaq-data-path
                         clojure.java.io/file
                         file-seq
                         rest
                         only-prices)]
    
    (doseq [a-data-file data-files]
      (let [lines (handle-data-file a-data-file :ignore-first)
            spl   (map
                   #(clojure.string/split % #",")
                   lines)
            outs  (map
                   #(str (nth % 1)
                         ","
                         (nth % 2)
                         ","
                         (nth % 3))
                   spl)]
        (doseq [out outs]
          (println out))))))

(defn open-prices->date-wise
  [open-prices-data]
  (let [lines (-> open-prices-data
                  io/reader
                  line-seq)

        data  (map
               #(clojure.string/split % #",")
               lines)]
    (println (take 10 data))))

(defn -main
  [& args]
  (let [in-file (first args)]
    (open-prices->date-wise in-file)))
