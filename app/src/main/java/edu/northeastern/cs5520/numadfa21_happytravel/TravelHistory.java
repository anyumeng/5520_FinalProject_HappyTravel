package edu.northeastern.cs5520.numadfa21_happytravel;

public class TravelHistory {
    private String place_id;
    private String review_content;
    private String review_photo_path;
    private String review_stars;
    private String review_time;
    private String type;
    private String user_id;
    public TravelHistory() {

    }

    public TravelHistory(String place_id, String review_content, String review_photo_path, String review_stars, String review_time, String type, String user_id) {
        this.place_id = place_id;
        this.review_content = review_content;
        this.review_photo_path = review_photo_path;
        this.review_stars = review_stars;
        this.review_time = review_time;
        this.type = type;
        this.user_id = user_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public String getReview_photo_path() {
        return review_photo_path;
    }

    public void setReview_photo_path(String review_photo_path) {
        this.review_photo_path = review_photo_path;
    }

    public String getReview_stars() {
        return review_stars;
    }

    public void setReview_stars(String review_stars) {
        this.review_stars = review_stars;
    }

    public String getReview_time() {
        return review_time;
    }

    public void setReview_time(String review_time) {
        this.review_time = review_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
