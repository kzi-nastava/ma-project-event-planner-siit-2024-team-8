package com.example.myapplication.domain;

import java.io.Serializable;

public class Review implements Serializable {
    private String id;
    private Double rating;
    private String comment;
    private String userName;
    private String userId;

    public String getId() {
        return id;
    }

    public Double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
