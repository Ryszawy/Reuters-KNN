package pl.ksr.classifier.metrics;

public enum MetricSelector {
    EUCLIDEAN(new EuclideanMetric()),
    MANHATTAN(new ManhattanMetric()),
    CHEBYSHEW(new ChebyshevMetric());
    private final Metric metric;

    MetricSelector(Metric selectedMetric) {
        metric = selectedMetric;
    }

    public Metric getMetric() {
        return metric;
    }
}
