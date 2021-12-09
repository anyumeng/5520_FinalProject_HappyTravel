package edu.northeastern.cs5520.numadfa21_happytravel;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserInfo {
    @Exclude
    private String key;
    private String userName;
    private String email;
    private String region;
    private String birthday;
    private String imageUrl;
    private String profileUrl;
    private Map<String, Integer> post;
    private ArrayList<String> friends = new ArrayList<>();
    public UserInfo() {
    }

    public UserInfo(String userName, String email, String region, String birthday,
                    String imageUrl, String profileUrl, Map<String, Integer> post,
                    ArrayList<String> friends) {
        this.userName = userName;
        this.email = email;
        this.region = region;
        this.birthday = birthday;
        this.imageUrl = imageUrl;
        this.profileUrl = profileUrl;
        this.post = post;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Map<String, Integer> getPost() {
        return post;
    }

    public void setPost(Map<String, Integer> post) {
        this.post = post;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "key='" + key + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", region='" + region + '\'' +
                ", birthday='" + birthday + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", post=" + post +
                ", friends=" + friends +
                '}';
    }
}
