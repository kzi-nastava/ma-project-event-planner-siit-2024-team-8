package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.domain.enumerations.Role;
import com.example.myapplication.domain.dto.UserInfoResponse;
import com.example.myapplication.fragments.asset.AssetCategoriesFragment;
import com.example.myapplication.fragments.asset.CreateAssetFragment;
import com.example.myapplication.fragments.asset.PriceListFragment;
import com.example.myapplication.services.UserService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileInfoFragment extends Fragment {

    private UserInfoResponse userInfo;

    public ProfileInfoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUserProfile();

        MaterialButton myAssetsButton = view.findViewById(R.id.my_assets_button);
        MaterialButton createAssetButton = view.findViewById(R.id.create_asset_button);
        MaterialButton assetCategoriesButton = view.findViewById(R.id.asset_categories_button);
        MaterialButton priceListButton = view.findViewById(R.id.price_list_button);

        myAssetsButton.setOnClickListener(v -> replaceFragment(new AllSolutionsFragment()));
        createAssetButton.setOnClickListener(v -> replaceFragment(new CreateAssetFragment()));
        assetCategoriesButton.setOnClickListener(v -> replaceFragment(new AssetCategoriesFragment()));
        priceListButton.setOnClickListener(v -> replaceFragment(new PriceListFragment()));

        Button button = view.findViewById(R.id.edit_button);
        button.setOnClickListener(v -> onEditClicked(userInfo));
    }

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadUserProfile() {
        // Retrieve the token from storage
        String token = JwtTokenUtil.getToken();

        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            UserService userService = new UserService(); // Create an instance of UserService
            userService.getApiService().getUserInfo(authHeader).enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        userInfo = response.body();
                        displayUserProfile(userInfo); // Handle the user profile UI
                    } else {
                        Log.e("Profile", "Failed to load profile: " + response.code());
                        try {
                            // Log the error body to understand why the request failed
                            Log.e("Profile", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e("Profile", "Error reading error body: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                    Log.e("Profile", "Error: " + t.getMessage());
                }
            });
        } else {
            Log.e("Profile", "Token is missing or invalid.");
        }
    }



    private void displayUserProfile(UserInfoResponse userInfo) {
        Log.d("ProfileImage", userInfo.profileImage);
        ImageView profileImage = getView().findViewById(R.id.profile_picture);
        Glide.with(this)
                .load(userInfo.profileImage)
                .placeholder(R.drawable.profile_placeholder) // Optional placeholder while loading// Optional error image
                .into(profileImage);

        TextView emailTextView = getView().findViewById(R.id.user_email);
        emailTextView.setText(userInfo.email);
        TextView fullNameView = getView().findViewById(R.id.user_fullname);
        fullNameView.setText(userInfo.firstName + userInfo.lastName);
        TextView addressView = getView().findViewById(R.id.user_address);
        addressView.setText(userInfo.address);
        TextView phoneView = getView().findViewById(R.id.user_phone);
        phoneView.setText(userInfo.number);
        TextView verifiedView = getView().findViewById(R.id.account_status);
        if (userInfo.isActive) {
            verifiedView.setText("Verified");
        } else {
            verifiedView.setText("Not Verified");
        }
        TextView nameView = getView().findViewById(R.id.user_name);
        nameView.setText(userInfo.firstName);
        TextView roleView = getView().findViewById(R.id.user_type);
        Role role = JwtTokenUtil.getRole();
        roleView.setText(String.valueOf(role));
        if (!String.valueOf(role).equalsIgnoreCase("PROVIDER")) {
            getView().findViewById(R.id.company_name_label).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.company_name).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.company_description_label).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.company_description).setVisibility(View.INVISIBLE);
        }
    }

    public void onEditClicked(UserInfoResponse userInfo) {
        ProfileEditFragment profileEditFragment = new ProfileEditFragment(userInfo);
        replaceFragment(profileEditFragment);
    }
}
