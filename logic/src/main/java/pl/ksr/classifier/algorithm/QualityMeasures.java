package pl.ksr.classifier.algorithm;

import pl.ksr.data.Article;

import java.util.ArrayList;
import java.util.List;

public class QualityMeasures {

    public static double accuracy(List<Article> testArticles) {
        int good = 0;
        for (Article article : testArticles) {
            if (article.getPlace().equals(article.getClassifiedPlace())) {
                good++;
            }
        }
        return (double) good / (double) testArticles.size();
    }

    public static double precision(List<Article> testArticles, String place) {
        int good = 0;
        List<Article> articles = testArticles.stream().filter(article ->
                article.getClassifiedPlace().equals(place)).toList();
        for (Article article : articles) {
            if (article.getPlace().equals(article.getClassifiedPlace())) {
                good++;
            }
        }
        return (double) good / (double) articles.size();
    }

    public static double recall(List<Article> testArticles, String place) {
        int good = 0;
        List<Article> articles = testArticles.stream().filter(article ->
                article.getClassifiedPlace().equals(place)).toList();
        List<Article> allArticles = testArticles.stream().filter(article ->
                article.getPlace().equals(place)).toList();
        for (Article article : articles) {
            if (article.getPlace().equals(article.getClassifiedPlace())) {
                good++;
            }
        }

        return (double) good / (double) allArticles.size();
    }


    public static double f1(List<Article> testArticles, String place) {
        return 2 * (precision(testArticles, place) * recall(testArticles, place))
                / (precision(testArticles, place) + recall(testArticles, place));
    }

    public static double calculateAverage(ArrayList<Double> results, ArrayList<Integer> weights) {
        int totalWeight = 0;
        double total = 0;
        for (int i = 0; i < results.size(); i++) {
            if (Double.compare(Double.NaN, results.get(i)) == 0) {
                totalWeight += weights.get(i);
                continue;
            }
            total += results.get(i) * weights.get(i);
            totalWeight += weights.get(i);
        }
        return total / (double) totalWeight;
    }
}
