package kr.clug.momukji;

public class restaurant_item {
    private String profile;
    private String title;
    private float starRating;
    private double latitude;
    private double longitude;
    static private double myLatitude = -1;
    static private double myLongitude = -1;
    private int uniqueId;

    public restaurant_item(int uniqueid, String profile, String title, float starRating, double latitude, double longitude) {
        this.uniqueId = uniqueid;
        this.profile = profile;
        this.title = title;
        this.starRating = starRating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getStarRating() {
        return starRating;
    }

    public void setStarRating(float starRating) {
        this.starRating = starRating;
    }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    static public double getMyLatitude() { return myLatitude; }

    static public void setMyLatitude(double latitude) { myLatitude = latitude; }

    static public double getMyLongitude() { return myLongitude; }

    static public void setMyLongitude(double longitude) { myLongitude = longitude; }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

}
