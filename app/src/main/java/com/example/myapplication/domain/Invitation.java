package com.example.myapplication.domain;

import java.time.LocalDate;

public class Invitation {
    private String email;
    private LocalDate date;

    private boolean isInput;

    public Invitation(String email){
        this.email = email;
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