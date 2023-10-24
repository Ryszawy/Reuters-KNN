package pl.ksr.data;

public class Article {
    private String place;
    private String[] title;
    private String[] body;

    private FeatureVector vector;

    private String classifiedPlace = "";

    public Article(String place, String[] title, String[] body) {
        this.place = place;
        this.title = title;
        this.body = body;
    }

    public String getPlace() {
        return place;
    }

    public String[] getTitle() {
        return title;
    }

    public String[] getBody() {
        return body;
    }

    public FeatureVector getVector() {
        return vector;
    }

    public void setVector(FeatureVector vector) {
        this.vector = vector;
    }

    public String getClassifiedPlace() {
        return classifiedPlace;
    }

    public void setClassifiedPlace(String classifiedPlace) {
        this.classifiedPlace = classifiedPlace;
    }
}
