(ns clj-dimension.estimation.demo
  "A demo of dimension estimation algorithms"
  (:use [clj-dimension.estimation.correlation-integral]))

(defn demo-grassberger-procaccia
  [& options]
  (if (some #{:show-plot})
    (estimate-dimension
     (get-dataset :iris)
     :show-plot)
    (estimate-dimension
     (get-dataset :iris))))
