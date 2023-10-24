package pl.ksr.classifier.similarity;

import java.util.ArrayList;

public class TextSimilarity {
    private int numberOfGrams;

    public TextSimilarity(int numberOfGrams) {
        this.numberOfGrams = numberOfGrams;
    }

    private ArrayList<String> ngrams(String a) {
        ArrayList<String> ngrams = new ArrayList<>();
        for (int i = 0; i < a.length() - numberOfGrams + 1; i++) {
            ngrams.add(a.substring(i, i + numberOfGrams));
        }
        return ngrams;
    }

    public double compareText(String a, String b) {
        if (a.equals("") && (b.equals(""))) {
            return 1.0;
        }
        if (a.equals("") || (b.equals(""))) {
            return 0.0;
        }
        ArrayList<String> firstNgrams = ngrams(a);
        ArrayList<String> secondNgrams = ngrams(b);
        int longer = firstNgrams.size();
        if (secondNgrams.size() > longer) {
            longer = secondNgrams.size();
        }
        int counter = 0;
        for (String first : firstNgrams) {
            for (String second: secondNgrams) {
                if (first.equalsIgnoreCase(second)) {
                    counter++;
                }
            }
        }
        return (double) counter / (double) longer;
    }

}
