package com.example.myapplication.services;

import com.example.myapplication.domain.User;
import com.example.myapplication.retrofits.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    private UserAPIService apiService;

    public UserService() {
        apiService = RetrofitClient.getRetrofitInstance().create(UserAPIService.class);
    }

    public void sendUserData(User user) {
        Call<User> call = apiService.registerUser(user);

        // Execute the request asynchronously
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // User data sent successfully
                    User returnedUser = response.body();
                    // Do something with the returned user, if needed
                } else {
                    // Handle failure (e.g., server error)
                    System.out.println("Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle error (e.g., network failure)
                t.printStackTrace();
            }
        });
    }
}
