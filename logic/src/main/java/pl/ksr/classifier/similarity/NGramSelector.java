package pl.ksr.classifier.similarity;

public enum NGramSelector {

    TRIGRAM(new TextSimilarity(3)),
    TETRAGRAM(new TextSimilarity(4));

    private TextSimilarity ngram;

    NGramSelector(TextSimilarity ngram) {
        this.ngram = ngram;
    }

    public TextSimilarity getNGram() {
        return ngram;
    }
}
