package edu.northeastern.cs5520.numadfa21_happytravel;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Post {
    private String imageUrl;
    private Context context;
    private String place;
    private String time;
    private String star;
    public Post() {

    }
    public Post(String imageUrl, Context context, String place, String time, String star) {
        this.imageUrl = imageUrl;
        this.context = context;
        this.place = place;
        this.time = time;
        this.star = star;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    @Override
    public String toString() {
        return "Post{" +
                "imageUrl='" + imageUrl + '\'' +
                ", context=" + context +
                ", place='" + place + '\'' +
                ", time='" + time + '\'' +
                ", star='" + star + '\'' +
                '}';
    }
}
