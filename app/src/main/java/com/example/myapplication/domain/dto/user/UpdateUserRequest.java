package com.example.myapplication.domain.dto.user;

import java.util.ArrayList;

public class UpdateUserRequest {
    public String firstName;
    public String lastName;
    public String email;
    public String number;
    public String address;
    public String companyName;
    public String companyDescription;
    public ArrayList<String> companyImagesURL;
}