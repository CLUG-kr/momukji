package kr.clug.momukji;

public class RestaurantRatingListItem {
    private double restRating;
    private String restRatingText;
    private String restRatingDate;

    public RestaurantRatingListItem(double restRating, String restRatingDate, String restRatingText) {
        this.restRating = restRating;
        this.restRatingDate = restRatingDate;
        this.restRatingText = restRatingText;

    }

    public void setRestRating(double rat) {
        restRating = rat;
    }
    public void setRestRatingDate(String ratdate) {
        restRatingDate = ratdate;
    }
    public void setRestRatingText(String rattext) {
        restRatingText = rattext;
    }

    public double getRestRating() {
        return this.restRating;
    }
    public String getRestRatingDate() {
        return this.restRatingDate;
    }
    public String getRestRatingText() {
        return this.restRatingText;
    }

}
