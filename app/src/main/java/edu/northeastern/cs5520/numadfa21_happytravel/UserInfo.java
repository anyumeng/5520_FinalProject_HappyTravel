package edu.northeastern.cs5520.numadfa21_happytravel;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UserInfo {
    @Exclude
    private String key;
    private String userName;
    private String region;
    private String birthday;
    private String imageUrl;
    private String profileUrl;
    private ArrayList<String> friends = new ArrayList<>();
    public UserInfo() {
    }

    public UserInfo(String userName, String region, String birthday, String imageUrl, String profileUrl, ArrayList<String> friends) {
        this.userName = userName;
        this.region = region;
        this.birthday = birthday;
        this.imageUrl = imageUrl;
        this.profileUrl = profileUrl;
        this.friends = friends;
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

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }
}
