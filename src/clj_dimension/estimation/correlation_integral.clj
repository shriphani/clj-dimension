(ns clj-dimension.estimation.correlation-integral
  "Estimate the correlation integral and use it to
   estimate the dimension of the dataset"
  (:use [incanter core io stats charts]))

(defn plot-corr-integral
  [dist-counts]
  (scatter-plot
   (map #(-> % first Math/log) (rest dist-counts))
   (map #(-> % second Math/log) (rest dist-counts))
   :x-label "log(Distance)"
   :y-label "log(Number of Pairs)"))

(defn corr-integral-slope
  [dist-counts]
  (let [xs (map #(-> % first Math/log) (rest dist-counts))
        ys (map #(-> % second Math/log) (rest dist-counts))]
    (simple-regression ys xs)))

(defn distance-vs-count
  "Expected data-structure:
   {[i j] <dist between points i and j>
    .
    .
    .}
   Returns for each dist how many pairs of points are within that
   neighborhood of each other"
  [pair-dist-map]
  (reverse
   (reduce
    (fn [acc [a-distance count]]
      (let [new-count (if (empty? acc)
                        count
                        (+ count (-> acc first second)))]
        (cons [a-distance new-count] acc)))
    []
    (map
     (fn [[a-distance points]]
       [a-distance (count points)])
     (sort-by
      first
      (reduce
       (fn [acc [a-pair dist]]
         (merge-with concat acc {dist [a-pair]}))
       {}
       (sort-by second pair-dist-map)))))))

(defn estimate-dimension-matrix
  "Estimate the dimension of the matrix"
  [the-matrix]
  (let [pair-dist-map (reduce
                       (fn [acc x]
                         (merge acc x))
                       {}
                       (for [i (range (nrow the-matrix))
                             j (range (inc i) (nrow the-matrix))]
                         {[i j] (euclidean-distance
                                 (nth the-matrix i)
                                 (nth the-matrix j))}))]
    (distance-vs-count pair-dist-map)))

(defn estimate-dimension
  "Estimate the dimension of the provided dataset
   using the grassberger procacia algorithm"
  [a-dataset & options]
  (let [the-matrix (to-matrix a-dataset)]
    (let [dist-counts (estimate-dimension-matrix the-matrix)
          plot-obj    (plot-corr-integral dist-counts)
          solution    (corr-integral-slope dist-counts)]
      (do
        (when (some #{:show-plot} options)
          (view plot-obj))
        {:estimated-dimension (-> solution :coefs last)
         :dataset-dimension   (ncol the-matrix)
         :solution            solution}))))
