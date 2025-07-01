package com.example.myapplication.domain.dto;

public class CreateReportRequest {
    private String userId;
    private String reportedEmail;
    private String reason;

    public CreateReportRequest(String reason, String reportedEmail, String userId) {
        this.reason = reason;
        this.reportedEmail = reportedEmail;
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReportedEmail() {
        return reportedEmail;
    }

    public void setReportedEmail(String reportedEmail) {
        this.reportedEmail = reportedEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}