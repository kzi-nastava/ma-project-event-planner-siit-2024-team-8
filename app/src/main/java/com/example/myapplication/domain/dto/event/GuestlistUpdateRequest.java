package com.example.myapplication.domain.dto.event;

import java.util.List;

public class GuestlistUpdateRequest {
    private List<String> userIds;

    public GuestlistUpdateRequest(List<String> userIds){
        this.userIds = userIds;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
