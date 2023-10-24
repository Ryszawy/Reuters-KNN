package pl.ksr.extractor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FeatureExtractor {
    public static boolean isPresent(String[] text, ArrayList<String> dictionary) {
        for (String word: text) {
            for (String keyWord: dictionary) {
                if (word.equalsIgnoreCase(keyWord)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getFirst(String[] text, ArrayList<String> dictionary) {
        for (String word: text) {
            for (String keyWord: dictionary) {
                if (word.equalsIgnoreCase(keyWord)) {
                    return word;
                }
            }
        }
        return "";
    }

    public static String getMostNumerous(String[] text, ArrayList<String> dictionary) {
        Map<String, Integer> map = new LinkedHashMap<>();

        for (String keyWord: dictionary) {
            int occurance = 0;
            for (String word : text) {
                if (word.equalsIgnoreCase(keyWord)) {
                    occurance++;
                }
            }
            if (occurance != 0) {
                map.put(keyWord,occurance);
            }
        }
        if (map.size() == 0) {
            return "";
        }
        if (map.size() == 1) {
            return map.keySet().stream().findFirst().get();
        }
        return map.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    public static String getLongest(String[] text) {
        return Stream.of(text).max(Comparator.comparing(String::length)).orElse("");
    }

    public static String getLongestFromDictionary(String[] text, ArrayList<String> dictionary) {
        ArrayList<String> keyWords = new ArrayList<>();
        for (String word: text) {
            for (String keyWord: dictionary) {
                if (word.equalsIgnoreCase(keyWord)) {
                    keyWords.add(keyWord);

                }
            }
        }
        return getLongest(keyWords.toArray(new String[keyWords.size()]));
    }

    public static String getLast(String[] text, ArrayList<String> dictionary) {
        for (int i = text.length - 1; i >= 0; i--) {
            for (String keyWord: dictionary) {
                if (text[i].equalsIgnoreCase(keyWord)) {
                    return keyWord;
                }
            }
        }
        return "";
    }

    public static double bigLetterCount(String[] text) {
        int counter = 0;
        for (String word: text) {
            if (Character.isUpperCase(word.charAt(0))) {
                counter++;
            }
        }
        if (text.length != 0) {
            return counter / (double) text.length;
        } else {
            return 0;
        }
    }

    public static double dictionaryCount(String[] text, ArrayList<String> dictionary) {
        int counter = 0;
        for (String word: text) {
            for (String keyWord: dictionary) {
                if (word.equalsIgnoreCase(keyWord)) {
                    counter++;
                }
            }
        }
        if (text.length != 0) {
            return counter / (double) text.length;
        } else {
            return 0;
        }
    }
}
