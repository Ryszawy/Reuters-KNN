package pl.ksr.data;

import pl.ksr.extractor.FeatureExtractor;

import java.util.ArrayList;

public class FeatureVector {
    private int currenciesInText;
    private int countriesInTitle;
    private String firstFromDictionaryInTitle;
    private String country;
    private String land;
    private String person;
    private String company;
    private String longestWord;
    private String longestWordFromDictionary;
    private String lastWordFromDictionary;
    private double bigLetterCount;
    private double dictionaryCount;

    private FeatureVector() {

    }

    public static Builder builder() {
        return new Builder();
    }

    public int getCurrenciesInText() {
        return currenciesInText;
    }

    public int getCountriesInTitle() {
        return countriesInTitle;
    }

    public String getFirstFromDictionaryInTitle() {
        return firstFromDictionaryInTitle;
    }

    public String getCountry() {
        return country;
    }

    public String getLand() {
        return land;
    }

    public String getPerson() {
        return person;
    }

    public String getCompany() {
        return company;
    }

    public String getLongestWord() {
        return longestWord;
    }

    public String getLongestWordFromDictionary() {
        return longestWordFromDictionary;
    }

    public String getLastWordFromDictionary() {
        return lastWordFromDictionary;
    }

    public double getBigLetterCount() {
        return bigLetterCount;
    }

    public double getDictionaryCount() {
        return dictionaryCount;
    }


    @Override
    public String toString() {
        return "["
                + currenciesInText + ", "
                + countriesInTitle + ", "
                + firstFromDictionaryInTitle + ", "
                + country + ", "
                + land + ", "
                + person + ", "
                + company + ", "
                + longestWord + ", "
                + longestWordFromDictionary  + ", "
                + lastWordFromDictionary + ", "
                + bigLetterCount + ","
                + dictionaryCount
                + "]";
    }

    public static final class Builder {
        private int currenciesInText = 0;
        private int countriesInTitle = 0;
        private String firstInDictionaryInTitle = "";
        private String country = "";
        private String land = "";
        private String person = "";
        private String company = "";
        private String longestWord = "";
        private String longestWordFromDictionary = "";
        private String lastWordFromDictionary = "";
        private double bigLetterCount = 0.0;
        private double dictionaryCount = 0.0;

        public Builder currenciesInText(String[] body, ArrayList<String> currencies) {
            this.currenciesInText = FeatureExtractor.isPresent(body,currencies) ? 1 : 0;
            return this;
        }

        public Builder countriesInTitle(String[] title, ArrayList<String> countries) {
            this.countriesInTitle = FeatureExtractor.isPresent(title,countries) ? 1 : 0;
            return this;
        }

        public Builder firstInDictionaryInTitle(String[] title, ArrayList<String> dictionary) {
            this.firstInDictionaryInTitle = FeatureExtractor.getFirst(title,dictionary);
            return this;
        }

        public Builder country(String[] body, ArrayList<String> countries) {
            this.country = FeatureExtractor.getMostNumerous(body,countries);
            return this;
        }

        public Builder land(String[] body, ArrayList<String> lands) {
            this.land = FeatureExtractor.getMostNumerous(body,lands);
            return this;
        }

        public Builder person(String[] body, ArrayList<String> people) {
            this.person = FeatureExtractor.getMostNumerous(body,people);
            return this;
        }

        public Builder company(String[] body, ArrayList<String> companies) {
            this.company = FeatureExtractor.getMostNumerous(body, companies);
            return this;
        }

        public Builder longestWord(String[] body) {
            this.longestWord = FeatureExtractor.getLongest(body);
            return this;
        }

        public Builder longestWordFromDictionary(String[] body, ArrayList<String> dictionary) {
            this.longestWordFromDictionary = FeatureExtractor
                    .getLongestFromDictionary(body, dictionary);
            return this;
        }

        public Builder lastWordFromDictionary(String[] body, ArrayList<String> dictionary) {
            this.lastWordFromDictionary = FeatureExtractor.getLast(body, dictionary);
            return this;
        }

        public Builder bigLetterCount(String[] body) {
            this.bigLetterCount = FeatureExtractor.bigLetterCount(body);
            return this;
        }

        public Builder dictionaryCount(String[] body, ArrayList<String> dictionary) {
            this.dictionaryCount = FeatureExtractor.dictionaryCount(body, dictionary);
            return this;
        }

        public FeatureVector build() {
            FeatureVector vector = new FeatureVector();
            vector.currenciesInText = this.currenciesInText;
            vector.countriesInTitle = this.countriesInTitle;
            vector.firstFromDictionaryInTitle = this.firstInDictionaryInTitle;
            vector.country = this.country;
            vector.land = this.land;
            vector.person = this.person;
            vector.company = this.company;
            vector.longestWord = this.longestWord;
            vector.longestWordFromDictionary = this.longestWordFromDictionary;
            vector.lastWordFromDictionary = this.lastWordFromDictionary;
            vector.bigLetterCount = this.bigLetterCount;
            vector.dictionaryCount = this.dictionaryCount;
            return vector;
        }

        public void setCurrenciesInText(int currenciesInText) {
            this.currenciesInText = currenciesInText;
        }

        public void setCountriesInTitle(int countriesInTitle) {
            this.countriesInTitle = countriesInTitle;
        }

        public void setFirstInDictionaryInTitle(String firstInDictionaryInTitle) {
            this.firstInDictionaryInTitle = firstInDictionaryInTitle;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setLand(String land) {
            this.land = land;
        }

        public void setPerson(String person) {
            this.person = person;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public void setLongestWord(String longestWord) {
            this.longestWord = longestWord;
        }

        public void setLongestWordFromDictionary(String longestWordFromDictionary) {
            this.longestWordFromDictionary = longestWordFromDictionary;
        }

        public void setLastWordFromDictionary(String lastWordFromDictionary) {
            this.lastWordFromDictionary = lastWordFromDictionary;
        }

        public void setBigLetterCount(double bigLetterCount) {
            this.bigLetterCount = bigLetterCount;
        }

        public void setDictionaryCount(double dictionaryCount) {
            this.dictionaryCount = dictionaryCount;
        }
    }

}
