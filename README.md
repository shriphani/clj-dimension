# clj_dimension

Shriphani learns to reduce dimensions.

Dataset: NASDAQ 1970 - 2010 from [here](http://www.infochimps.com/datasets/nasdaq-exchange-daily-1970-2010-open-close-high-low-and-volume/)

Implemented so far:

 * Grassberger-Proccacia algorithm: [[src](src/clj-dimension/estimation/correlation_integral.clj)/[demo](src/clj-dimension/estimation/demo.clj)]. Explanations: [here](http://www.scholarpedia.org/article/Grassberger-Procaccia_algorithm) and [here](http://research.microsoft.com/en-us/um/people/cburges/tech_reports/msr-tr-2009-2013.pdf) - warning, pdf!