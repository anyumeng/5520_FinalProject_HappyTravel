package edu.northeastern.cs5520.numadfa21_happytravel;

public class FriendPost {
    String imageUrl;
    String userName;
    String placeName;
    String content;
    float reviewStars;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getReviewStars() {
        return reviewStars;
    }

    public void setReviewStars(float reviewStars) {
        this.reviewStars = reviewStars;
    }

    @Override
    public String toString() {
        return "FriendPost{" +
                "imageUrl='" + imageUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", placeName='" + placeName + '\'' +
                ", content='" + content + '\'' +
                ", reviewStars=" + reviewStars +
                '}';
    }
}
