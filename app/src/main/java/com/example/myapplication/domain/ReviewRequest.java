package com.example.myapplication.domain;

public class ReviewRequest {
    private String comment;
    private int rating;
    private String userId;

    public ReviewRequest(String comment, int rating, String userId) {
        this.comment = comment;
        this.rating = rating;
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

