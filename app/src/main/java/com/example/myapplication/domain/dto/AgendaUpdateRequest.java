package com.example.myapplication.domain.dto;

import java.util.List;

public class AgendaUpdateRequest {
    private List<ActivityUpdateRequest> activityUpdates;

    public AgendaUpdateRequest(List<ActivityUpdateRequest> activityUpdates) {
        this.activityUpdates = activityUpdates;
    }

    public List<ActivityUpdateRequest> getActivityUpdates() {
        return activityUpdates;
    }

    public void setActivityUpdates(List<ActivityUpdateRequest> activityUpdates) {
        this.activityUpdates = activityUpdates;
    }

    public AgendaUpdateRequest() {}
}
