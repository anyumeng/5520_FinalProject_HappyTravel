package edu.northeastern.cs5520.numadfa21_happytravel.bean;

import java.io.Serializable;

public class CommonBean implements Serializable {
    private String ranking;
    private String name;
    private String stars;

    public String getCode() {
        return ranking;
    }

    public void setCode(String ranking) {
        this.ranking = ranking;
    }

    public String getText() {
        return name;
    }

    public void setText(String name) {
        this.name = name;
    }

    public String getTest() {
        return stars;
    }

    public void setTest(String stars) {
        this.stars = stars;
    }
}
