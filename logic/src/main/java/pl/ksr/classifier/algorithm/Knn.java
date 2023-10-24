package pl.ksr.classifier.algorithm;

import pl.ksr.classifier.metrics.MetricSelector;
import pl.ksr.classifier.similarity.NGramSelector;
import pl.ksr.data.Article;
import pl.ksr.data.FeatureVector;
import pl.ksr.data.PlacesList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class Knn {
    public static void classify(List<Article> learningSet, Article article, int k,
                                NGramSelector ngramSelector, MetricSelector metricSelector) {
        Map<Article, Double> nearestNeighbours = new LinkedHashMap<>();
        nearestNeighbours = findkNN(learningSet, article, k, ngramSelector, metricSelector);
        classifyArticle(article, nearestNeighbours);
    }


    private static double calculateDistance(FeatureVector vector1, FeatureVector vector2,
                       NGramSelector ngramSelector, MetricSelector metricSelector) {
        return metricSelector.getMetric().getDistance(vector1, vector2, ngramSelector);
    }

    private static Map<Article, Double> findkNN(List<Article> learningSet, Article article,
                          int k, NGramSelector ngramSelector, MetricSelector metricSelector) {
        Map<Article, Double> distances = new LinkedHashMap<>();
        for (Article learningArticle : learningSet) {
            distances.put(learningArticle, calculateDistance(article.getVector(),
                    learningArticle.getVector(), ngramSelector, metricSelector));
        }
        distances = sort(distances, k);
        return distances;

    }

    private static void classifyArticle(Article article, Map<Article, Double> neighbours) {
        Map<String, Integer> occurrenceMap = new LinkedHashMap<>();
        for (String place : PlacesList.places) {
            occurrenceMap.put(place, getOccurrences(place, neighbours));
        }
        ArrayList<String> places = findMaxOccurrences(occurrenceMap);
        if (places.size() == 1) {
            article.setClassifiedPlace(places.get(0));
        } else {
            Map<String, Double> totalDistanceMap = new LinkedHashMap<>();
            for (String place : places) {
                totalDistanceMap.put(place, getTotalDistance(place, neighbours));
            }
            article.setClassifiedPlace(findMinDistance(totalDistanceMap));
        }
    }


    private static double getTotalDistance(String place, Map<Article, Double> neighbours) {
        double distance = 0;
        for (Map.Entry<Article, Double> entry : neighbours.entrySet()) {
            if (entry.getKey().getPlace().equals(place)) {
                distance += entry.getValue();
            }
        }
        return distance;
    }

    private static int getOccurrences(String place, Map<Article, Double> neighbours) {
        int counter = 0;
        for (Map.Entry<Article, Double> entry : neighbours.entrySet()) {
            if (entry.getKey().getPlace().equals(place)) {
                counter++;
            }
        }
        return counter;
    }

    private static ArrayList<String> findMaxOccurrences(Map<String, Integer> map) {
        int max = Collections.max(map.values());
        ArrayList<String> places = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == max) {
                places.add(entry.getKey());
            }
        }
        return places;
    }

    private static String findMinDistance(Map<String, Double> map) {
        double min = Collections.min(map.values());
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            if (entry.getValue() == min) {
                return entry.getKey();
            }
        }
        throw new RuntimeException();
    }

    private static Map<Article, Double> sort(Map<Article, Double> map, int k) {
        List<Map.Entry<Article, Double>> list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Article, Double>>() {
            public int compare(Map.Entry<Article, Double> o1, Map.Entry<Article, Double> o2) {

                return o1.getValue().compareTo(o2.getValue());
            }
        });
        Map<Article, Double> sortedMap = new LinkedHashMap<Article, Double>();
        for (int i = 0; i < k; i++) {
            sortedMap.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return sortedMap;
    }

}
