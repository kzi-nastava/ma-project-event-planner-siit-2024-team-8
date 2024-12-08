package com.example.myapplication.services;

import com.example.myapplication.domain.User;
import com.example.myapplication.retrofits.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    private final UserAPIService apiService;

    public UserService() {
        apiService = RetrofitClient.getRetrofitInstance().create(UserAPIService.class);
    }
}
