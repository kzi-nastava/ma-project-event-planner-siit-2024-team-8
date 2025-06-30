package com.example.myapplication.services;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.callbacks.UserRegisterCallBack;
import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.enumerations.Role;
import com.example.myapplication.domain.dto.UserCreateRequest;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.utilities.RetrofitClient;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    private final UserAPIService apiService;

    public UserService() {
        apiService = RetrofitClient.getRetrofitInstance().create(UserAPIService.class);
    }

    public UserAPIService getApiService() {
        return this.apiService;
    }

    public void createUser(UserCreateRequest user, File file, UserRegisterCallBack callback) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody userRequestBody = RequestBody.create(MediaType.parse("application/json"), json);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        if (user.getUserType() == Role.USER || user.getUserType() == Role.ORGANIZER) {
            ClientUtils.userService.registerUser(userRequestBody, imagePart).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        try {
                            String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown server error";
                            callback.onServerError(errorMessage);
                        } catch (IOException e) {
                            callback.onServerError("Server error occurred.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("RegisterUser", "Network error: ", t);
                    callback.onNetworkError(t);
                }
            });
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
