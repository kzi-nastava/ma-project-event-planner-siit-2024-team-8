package com.example.myapplication.services;

import com.example.myapplication.domain.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPIService {
    @POST("/users/register")
    Call<User> registerUser(@Body User user);
}