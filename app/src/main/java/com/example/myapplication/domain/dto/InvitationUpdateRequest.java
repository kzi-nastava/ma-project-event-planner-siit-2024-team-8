package com.example.myapplication.domain.dto;

import com.example.myapplication.domain.Invitation;

import java.util.List;

public class InvitationUpdateRequest {
    private List<String> emails;

    public InvitationUpdateRequest(List<String> emails){
        this.emails = emails;
    }
}