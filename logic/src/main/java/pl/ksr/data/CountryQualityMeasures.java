package pl.ksr.data;

import pl.ksr.classifier.algorithm.QualityMeasures;

import java.text.DecimalFormat;
import java.util.List;

public class CountryQualityMeasures {

    private List<Article> testArticles;
    private String place;

    private double precision;
    private double recall;
    private double f1;


    private int totalNumberOfBelonging;


    public CountryQualityMeasures(List<Article> testArticles, String place) {
        this.testArticles = testArticles;
        this.place = place;
        this.precision = QualityMeasures.precision(testArticles,place);
        this.recall = QualityMeasures.recall(testArticles, place);
        this.f1 = QualityMeasures.f1(testArticles,place);
        this.totalNumberOfBelonging = calculateNumberOfBelonging();
    }

    private int calculateNumberOfClassified(String country) {
        List<Article> articles = testArticles.stream().filter(article ->
                article.getClassifiedPlace().equals(place)).toList();
        articles = articles.stream().filter(article ->
                article.getPlace().equals(country)).toList();

        return articles.size();
    }

    private int calculateNumberOfBelonging() {
        List<Article> articles = testArticles.stream().filter(article ->
                article.getPlace().equals(place)).toList();
        return articles.size();
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return  place.toUpperCase() + ":"
                + "  PPV: " + df.format(precision)
                + "  TPR: " + df.format(recall)
                + "  F1: " + df.format(f1);
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double getF1() {
        return f1;
    }

    public String getTotalNumberOfClassified() {
        StringBuilder builder = new StringBuilder();
        for (String country: PlacesList.places) {
            builder.append(country.toUpperCase());
            builder.append(": ");
            builder.append(calculateNumberOfClassified(country));
            builder.append(" ");
        }
        return builder.toString();
    }

    public int getTotalNumberOfBelonging() {
        return totalNumberOfBelonging;
    }
}
