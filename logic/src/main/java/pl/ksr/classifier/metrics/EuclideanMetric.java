package pl.ksr.classifier.metrics;

import pl.ksr.classifier.similarity.NGramSelector;
import pl.ksr.data.FeatureVector;

public class EuclideanMetric extends Metric {

    @Override
    protected double calculateSingleDimension(double first, double second) {
        return Math.pow(first - second, 2);
    }

    @Override
    public double getDistance(FeatureVector vector1, FeatureVector vector2,
                              NGramSelector selector) {
        double distance = this.calculate(vector1,vector2, selector);
        return Math.sqrt(distance);
    }
}
