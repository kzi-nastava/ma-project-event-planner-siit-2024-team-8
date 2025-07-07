package com.example.myapplication.domain.dto;

public class MarkSeenRequest {
    private String userId;
    private String otherUserId;

    public MarkSeenRequest(String userId, String otherUserId) {
        this.userId = userId;
        this.otherUserId = otherUserId;
    }
}
