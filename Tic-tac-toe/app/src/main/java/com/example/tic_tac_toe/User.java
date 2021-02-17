package com.example.tic_tac_toe;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;
    private String username;
    private String profileImage;

    public User() {
        this.uid = "";
        this.username = "";
        this.profileImage = "";
    }

    public User(String uid, String username, String profileImageUrl){
        this.uid = uid;
        this.username = username;
        this.profileImage = profileImageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImage = profileImageUrl;
    }
}