package com.example.myapplication.domain;

public class Utility extends Asset {

    private int duration;
    private String reservationTerm;
    private String cancellationTerm;
    private boolean manuelConfirmation;

    // Getters and setters
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getReservationTerm() {
        return reservationTerm;
    }

    public void setReservationTerm(String reservationTerm) {
        this.reservationTerm = reservationTerm;
    }

    public String getCancellationTerm() {
        return cancellationTerm;
    }

    public void setCancellationTerm(String cancellationTerm) {
        this.cancellationTerm = cancellationTerm;
    }

    public boolean isManuelConfirmation() {
        return manuelConfirmation;
    }

    public void setManuelConfirmation(boolean manuelConfirmation) {
        this.manuelConfirmation = manuelConfirmation;
    }
}
