package com.example.myapplication.services;

import android.util.Log;

import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.Role;
import com.example.myapplication.domain.dto.UpdateUserRequest;
import com.example.myapplication.domain.dto.UserCreateRequest;
import com.example.myapplication.utilities.RetrofitClient;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class UserService {

    private final UserAPIService apiService;

    public UserService() {
        apiService = RetrofitClient.getRetrofitInstance().create(UserAPIService.class);
    }

    public UserAPIService getApiService() {
        return this.apiService;
    }

    public void createUser(UserCreateRequest user,File file) {
        Gson gson = new Gson();
        String json = gson.toJson(user); // Convert object to JSON string
        RequestBody userRequestBody = RequestBody.create(MediaType.parse("application/json"), json);


        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        
        if (user.getUserType() == Role.USER || user.getUserType() == Role.ORGANIZER) {
            ClientUtils.userService.registerUser(userRequestBody,imagePart).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d("RegisterUser", "User registered successfully: ");
                    } else {
                        // Handle server error
                        try {
                            // Log the response code and error body
                            String errorMessage = response.errorBody().string(); // Get the error body as a string
                            Log.e("RegisterUser", "Server error: " + response.code() + " - " + errorMessage);
                        } catch (IOException e) {
                            Log.e("RegisterUser", "Error while reading the error body", e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    // Handle network failure
                    Log.e("RegisterUser", "Network error: ", t);
                }
            });

        } else {
        }
    }

    public void updateUser(RequestBody firstName, RequestBody lastName, RequestBody email,
                           RequestBody address, RequestBody number, MultipartBody.Part image,
                           Callback<String> callback) {

        // Call to the backend API with multipart data
        Call<String> call = apiService.updateUser(firstName, lastName, email, address, number, image);
        call.enqueue(callback);
    }

}
