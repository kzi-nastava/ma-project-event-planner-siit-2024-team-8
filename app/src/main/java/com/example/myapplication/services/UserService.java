package com.example.myapplication.services;

import android.util.Log;

import com.example.myapplication.domain.dto.UpdateUserRequest;
import com.example.myapplication.utilities.RetrofitClient;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;

public class UserService {

    private final UserAPIService apiService;

    public UserService() {
        apiService = RetrofitClient.getRetrofitInstance().create(UserAPIService.class);
    }

    public UserAPIService getApiService() {
        return this.apiService;
    }

    public void updateUser(RequestBody firstName, RequestBody lastName, RequestBody email,
                           RequestBody address, RequestBody number, MultipartBody.Part image,
                           Callback<String> callback) {

        // Call to the backend API with multipart data
        Call<String> call = apiService.updateUser(firstName, lastName, email, address, number, image);
        call.enqueue(callback);
    }

}
