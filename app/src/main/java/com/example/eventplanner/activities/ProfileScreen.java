package com.example.eventplanner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.fragments.ProfileInfoFragment;

public class ProfileScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        String userName = "Johnny Boi";
        String userEmail = "john.boy@example.com";
        String userFullName = "John Brother";
        String userAddress = "123 Main Street";
        String userPhone = "123-456-7890";
        String accountStatus = "Active";
        String companyName = "John's Co.";
        String companyDescription = "We provide excellent services.";

        SharedPreferences sharedPreferences = getSharedPreferences("userProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_NAME", userName);
        editor.putString("USER_EMAIL", userEmail);
        editor.putString("USER_FULL_NAME", userFullName);
        editor.putString("USER_ADDRESS", userAddress);
        editor.putString("USER_PHONE", userPhone);
        editor.putString("ACCOUNT_STATUS", accountStatus);
        editor.putString("COMPANY_NAME", companyName);
        editor.putString("COMPANY_DESCRIPTION", companyDescription);
        editor.apply();

        Bundle args = new Bundle();
        args.putString("USER_NAME", userName);
        args.putString("USER_EMAIL", userEmail);
        args.putString("USER_FULL_NAME", userFullName);
        args.putString("USER_ADDRESS", userAddress);
        args.putString("USER_PHONE", userPhone);
        args.putString("ACCOUNT_STATUS", accountStatus);
        args.putString("COMPANY_NAME", companyName);
        args.putString("COMPANY_DESCRIPTION", companyDescription);

        if (savedInstanceState == null) {
            ProfileInfoFragment profileInfoFragment = new ProfileInfoFragment();
            profileInfoFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.profile_fragment_container, profileInfoFragment)
                    .commit();
        }
    }

    public void onClickNavbarButton(View view) {
        /*if (view.getId() == R.id.searchButton) {
            startActivity(new Intent(ProfileScreen.this, LoginScreen.class));
        }*/
        if (view.getId() == R.id.homeButton) {
            startActivity(new Intent(ProfileScreen.this, HomeScreen.class));
        }
    }
}
