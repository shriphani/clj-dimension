(ns clj-dimension.process-data
  "Deal with the usual mess of data")

(defn read-csv
  "Expects comma-separated file. Relies on jvm to close fd"
  [a-csv-file & kwargs]
  (let [line-op (fn [a-line]
                  (clojure.string/split a-line
                                        #","))
        
        rs      (->> a-csv-file
                     clojure.java.io/reader
                     line-seq
                     (map line-op))

        ign-fst (:ignore-first (apply hash-map kwargs))]
    
    (if ign-fst (rest rs) rs)))

(defn handle-lines
  [lines-seq]
  (map #(-> [(nth % 1)
             (nth % 2)
             (nth % 3)])
       lines-seq))

(defn stream->map
  "Expected data-format [Ticker Item]"
  [a-stream]
  (reduce
   (fn [acc [ticker d x]]
     (merge-with concat acc {ticker [[d x]]}))
   {}
   a-stream))

(defn write-data
  [loc symbols mapped]
  (let [row-one (clojure.string/join "," symbols)

        data    (apply map vector (map #(mapped %) symbols))]
    
    (with-open [wrtr (clojure.java.io/writer loc)]
      (binding [*out* wrtr]
        (do (println row-one)
            (doseq [symbol symbols]
              '*))))))

(defn nasdaq->stock-wise
  "Produce a NASDAQ table where columns = companies and rows = days with only
   opening price

   Args:
    nasdaq-data-path : the path to the nasdaq dataset
    destination-csv : "
  [nasdaq-data-path destination-csv]
  (let [data-files (->> nasdaq-data-path
                        clojure.java.io/file
                        file-seq
                        rest
                        (filter
                         (fn [a-data-file]
                           (re-find #"daily_prices"
                                    (.getAbsolutePath
                                     a-data-file)))))
        
        stream     (map
                    (fn [a-data-file]
                      (-> a-data-file
                          (read-csv :ignore-first true)
                          handle-lines))
                    data-files)]
    
    stream))
