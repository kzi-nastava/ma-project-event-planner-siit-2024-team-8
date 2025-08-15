package com.example.myapplication.domain;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {

    private UUID id;

    private UUID notifiedUserId;

    private String title;

    private String body;

    private boolean seen = false;

    private boolean clickable = false;

    private UUID reservationId = null;

    private String timestamp;

    public static Notification fromJson(String json) {
        return new Gson().fromJson(json,Notification.class);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getNotifiedUserId() {
        return notifiedUserId;
    }

    public void setNotifiedUserId(UUID notifiedUserId) {
        this.notifiedUserId = notifiedUserId;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}