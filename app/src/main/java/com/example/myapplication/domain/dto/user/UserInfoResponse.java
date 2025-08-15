package com.example.myapplication.domain.dto.user;


import com.example.myapplication.domain.enumerations.Role;

public class UserInfoResponse {
    public String firstName;
    public String lastName;
    public String email;
    public String number;
    public String profileImage;
    public String address;
    public Role role;
    public Boolean isActive;
}
