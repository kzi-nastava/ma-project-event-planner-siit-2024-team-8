package com.example.myapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.domain.User;
import com.example.myapplication.domain.UserType;
import com.example.myapplication.services.ClientUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public UserViewModel() {
        // Initialize with an empty user
        userLiveData.setValue(new User());
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void onUserTypeChanged(){
    }

    // Method to save the user data (you can add logic to handle validation here)
    public void saveUserData() {
        User user = userLiveData.getValue();
        if (user.getUserType() == UserType.USER || user.getUserType() == UserType.ORGANIZER) {

            ClientUtils.userService.registerUser(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        // Success
                        User registeredUser = response.body();
                        Log.d("RegisterUser", "User registered successfully: " + registeredUser);
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
                public void onFailure(Call<User> call, Throwable t) {
                    // Handle network failure
                    Log.e("RegisterUser", "Network error: ", t);
                }
            });

        }else{
            if (isValidProvider(user)){
                ClientUtils.userService.registerUser(user);
            }
        }

    }

    private boolean isValidUser(User user){
        return (user!=null && !user.getFirstName().isEmpty() && !user.getLastName().isEmpty() && !user.getEmail().isEmpty()
        && !user.getPassword().isEmpty() && !user.getNumber().isEmpty() && !user.getProfileImageURL().isEmpty() && !user.getAddress().isEmpty());
    }
    private boolean isValidProvider(User user){
        return isValidUser(user) && !user.getCompanyName().isEmpty() && !user.getCompanyDescription().isEmpty() && !user.getCompanyImagesURL().isEmpty();
    }

}
