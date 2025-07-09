package com.example.myapplication.domain;

import com.example.myapplication.domain.dto.GuestResponse;

import java.time.LocalDate;

public class Invitation {
    private String userId;

    private String name;
    private String email;
    private LocalDate date;

    private boolean isInput;

    public Invitation(String email){
        this.email = email;
    }
    public Invitation(GuestResponse guestResponse){
        this.email = guestResponse.email;
        this.userId = guestResponse.userId;
        this.name = guestResponse.name;
    }

    public void setInput(boolean input) {
        isInput = input;
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

    public Invitation(String email, LocalDate date) {
        this.email = email;
        this.date = date;
    }

    public Invitation(LocalDate date, String email, boolean isInput) {
        this.date = date;
        this.email = email;
        this.isInput = isInput;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isInput (){
        return isInput;
    }
}