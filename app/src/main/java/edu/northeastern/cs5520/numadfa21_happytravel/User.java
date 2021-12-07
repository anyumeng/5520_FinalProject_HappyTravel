package edu.northeastern.cs5520.numadfa21_happytravel;

import java.util.Map;

public class User {

    private String userName;
    private Map<String, Integer> post;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, Integer> getPost() {
        return post;
    }

    public void setPost(Map<String, Integer> post) {
        this.post = post;
    }
}
