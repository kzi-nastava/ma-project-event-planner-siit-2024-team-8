package com.example.myapplication.domain;

import com.google.gson.Gson;

public class Message {
    private String senderId;
    private String receiverId;
    private String messageContent;
    private String sentAt;
    private boolean deleted;

    // Getters/setters omitted for brevity

    public static Message fromJson(String json) {
        return new Gson().fromJson(json, Message.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSentAt() {
        return sentAt;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }
}
