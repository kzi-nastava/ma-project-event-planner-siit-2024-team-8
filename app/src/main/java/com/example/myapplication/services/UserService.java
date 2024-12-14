package com.example.myapplication.services;

import com.example.myapplication.utilities.RetrofitClient;

public class UserService {

    private final UserAPIService apiService;

    public UserService() {
        apiService = RetrofitClient.getRetrofitInstance().create(UserAPIService.class);
    }
}
