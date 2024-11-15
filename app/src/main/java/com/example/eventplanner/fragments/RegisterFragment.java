package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventplanner.R;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Back button to go back to HomeFragment
        // DEPRECATED - use navbar instead
        //view.findViewById(R.id.backButton).setOnClickListener(v -> navigateToHome());

        // Login Redirect button to go to LoginFragment
        view.findViewById(R.id.loginRedirectButton).setOnClickListener(v -> navigateToLogin());

        // Handle other button clicks or actions here as needed

        return view;
    }

    /*
    DEPRECATED - use navbar instead
    private void navigateToHome() {
        UserHomeFragment userHomeFragment = new UserHomeFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, userHomeFragment)
                .addToBackStack(null)
                .commit();
    }*/

    private void navigateToLogin() {
        LoginFragment loginFragment = new LoginFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, loginFragment)
                .addToBackStack(null)
                .commit();
    }
}
