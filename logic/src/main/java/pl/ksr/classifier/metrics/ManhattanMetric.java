package pl.ksr.classifier.metrics;

import pl.ksr.classifier.similarity.NGramSelector;
import pl.ksr.data.FeatureVector;

public class ManhattanMetric extends Metric {
    @Override
    protected double calculateSingleDimension(double first, double second) {
        return Math.abs(first - second);
    }

    @Override
    public double getDistance(FeatureVector vector1, FeatureVector vector2,
                              NGramSelector selector) {
        return this.calculate(vector1, vector2, selector);
    }
}
