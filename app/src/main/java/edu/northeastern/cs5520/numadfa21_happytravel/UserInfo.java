package edu.northeastern.cs5520.numadfa21_happytravel;

import com.google.firebase.database.Exclude;

public class UserInfo {
    @Exclude
    private String key;
    private String userName;
    private String region;
    private String birthday;
    private String imageUrl;
    private String profileUrl;
    public UserInfo() {
    }

    public UserInfo(String userName, String region, String birthday, String imageUrl, String profileUrl) {
        this.userName = userName;
        this.region = region;
        this.birthday = birthday;
        this.imageUrl = imageUrl;
        this.profileUrl = profileUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
