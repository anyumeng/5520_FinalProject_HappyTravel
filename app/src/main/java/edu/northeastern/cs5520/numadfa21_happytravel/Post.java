package edu.northeastern.cs5520.numadfa21_happytravel;

public class Post {
    private String imageUrl;
    private String context;

    public Post(String imageUrl, String context) {
        this.imageUrl = imageUrl;
        this.context = context;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
