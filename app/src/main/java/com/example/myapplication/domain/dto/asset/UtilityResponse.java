package com.example.myapplication.domain.dto.asset;

import com.example.myapplication.domain.dto.user.AssetResponse;

import java.util.UUID;

public class UtilityResponse extends AssetResponse {
    private Double price;
    private Double discount;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
    private Boolean visible;
    private Boolean available;
    private String status;
    private Integer duration;
    private Integer  reservationTerm;
    private Integer  cancellationTerm;
    private Boolean manuelConfirmation;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer  getCancellationTerm() {
        return cancellationTerm;
    }

    public void setCancellationTerm(Integer  cancellationTerm) {
        this.cancellationTerm = cancellationTerm;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getManuelConfirmation() {
        return manuelConfirmation;
    }

    public void setManuelConfirmation(Boolean manuelConfirmation) {
        this.manuelConfirmation = manuelConfirmation;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getReservationTerm() {
        return reservationTerm;
    }

    public void setReservationTerm(Integer  reservationTerm) {
        this.reservationTerm = reservationTerm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}