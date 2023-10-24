package pl.ksr.data;

public enum Places {
    CANADA("canada"),
    FRANCE("france"),
    JAPAN("japan"),
    UK("uk"),
    USA("usa"),
    WEST_GERMANY("west-germany");

    private String place;

    Places(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }
}
