package com.example.myapplication.domain;

import java.time.LocalDateTime;

public class InboxUser {
    private String userId;
    private String fullName;
    private String role;
    private boolean hasUnreadMessage;
    private String lastMessageTime;

    public InboxUser() {}

    public InboxUser(String userId, String fullName, String role, boolean hasUnreadMessage, String lastMessageTime) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = role;
        this.hasUnreadMessage = hasUnreadMessage;
        this.lastMessageTime = lastMessageTime;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public boolean hasUnreadMessage() {
        return hasUnreadMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setHasUnreadMessage(boolean hasUnreadMessage) {
        this.hasUnreadMessage = hasUnreadMessage;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    // Optional: for sorting
    public long getLastMessageTimeMillis() {
        try {
            return java.time.Instant.parse(lastMessageTime).toEpochMilli();
        } catch (Exception e) {
            return 0;
        }
    }
}
