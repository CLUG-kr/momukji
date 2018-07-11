package kr.clug.momukji;

public class restaurant_item {
    private int profile;
    private String title;
    private float starRating;
    private int distance;
    private int uniqueId;

    public restaurant_item(int uniqueid, int profile, String title, float starRating, int distance) {
        this.uniqueId = uniqueid;
        this.profile = profile;
        this.title = title;
        this.starRating = starRating;
        this.distance = distance;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

}
