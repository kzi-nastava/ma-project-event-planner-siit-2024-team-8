package com.example.myapplication.domain.dto;

import com.example.myapplication.domain.Invitation;

public class GuestResponse {
    public String name;
    public String email;
    public String userId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public GuestResponse (Invitation invitation){
        this.email = invitation.getEmail();
        this.userId = invitation.getUserId();
        this.name = invitation.getName();
    }
}
