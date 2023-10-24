package pl.ksr.classifier.metrics;

import pl.ksr.classifier.similarity.NGramSelector;
import pl.ksr.data.FeatureVector;

public abstract class Metric {
    public double calculate(FeatureVector vector1, FeatureVector vector2, NGramSelector selector) {
        double distance = 0;
        distance += calculateSingleDimension(vector1.getCurrenciesInText(),
                vector2.getCurrenciesInText());
        distance += calculateSingleDimension(vector1.getCountriesInTitle(),
                vector2.getCountriesInTitle());
        distance += calculateSingleDimension(1, selector.getNGram().compareText(
                vector1.getFirstFromDictionaryInTitle(), vector2.getFirstFromDictionaryInTitle()));
        distance += calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getCountry(), vector2.getCountry()));
        distance += calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getLand(), vector2.getLand()));
        distance += calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getPerson(), vector2.getPerson()));
        distance += calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getCompany(), vector2.getCompany()));
        distance += calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getLongestWord(), vector2.getLongestWord()));
        distance += calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getLongestWordFromDictionary(), vector2.getLongestWordFromDictionary()));
        distance += calculateSingleDimension(1,selector.getNGram().compareText(
                vector1.getLastWordFromDictionary(), vector2.getLastWordFromDictionary()));
        distance += calculateSingleDimension(vector1.getBigLetterCount(),
                vector2.getBigLetterCount());
        distance += calculateSingleDimension(vector1.getDictionaryCount(),
                vector2.getDictionaryCount());
        return distance;
    }

    protected abstract double calculateSingleDimension(double first, double second);

    public abstract double getDistance(FeatureVector vector1, FeatureVector vector2,
                                       NGramSelector selector);


}
