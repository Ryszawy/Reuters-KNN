package pl.ksr.classifier.metrics;

import pl.ksr.classifier.similarity.NGramSelector;
import pl.ksr.data.FeatureVector;

import java.util.ArrayList;
import java.util.Collections;

public class ChebyshevMetric extends Metric {
    @Override
    public double calculate(FeatureVector vector1, FeatureVector vector2, NGramSelector selector) {
        ArrayList<Double> distance = new ArrayList<>();
        distance.add(calculateSingleDimension(vector1.getCurrenciesInText(),
                vector2.getCurrenciesInText()));
        distance.add(calculateSingleDimension(vector1.getCountriesInTitle(),
                vector2.getCountriesInTitle()));
        distance.add(calculateSingleDimension(1, selector.getNGram().compareText(
                vector1.getFirstFromDictionaryInTitle(),
                vector2.getFirstFromDictionaryInTitle())));
        distance.add(calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getCountry(), vector2.getCountry())));
        distance.add(calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getLand(), vector2.getLand())));
        distance.add(calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getPerson(), vector2.getPerson())));
        distance.add(calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getCompany(), vector2.getCompany())));
        distance.add(calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getLongestWord(), vector2.getLongestWord())));
        distance.add(calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getLongestWordFromDictionary(), vector2.getLongestWordFromDictionary())));
        distance.add(calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getLastWordFromDictionary(),
                vector2.getLastWordFromDictionary())));
        distance.add(calculateSingleDimension(vector1.getBigLetterCount(),
                vector2.getBigLetterCount()));
        distance.add(calculateSingleDimension(vector1.getDictionaryCount(),
                vector2.getDictionaryCount()));
        return Collections.max(distance);
    }

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
