package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventplanner.R;
import com.example.eventplanner.fragments.RegisterFragment;
import com.example.eventplanner.fragments.UserHomeFragment;

public class LoginFragment extends Fragment {

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //view.findViewById(R.id.backButton2).setOnClickListener(v -> navigateToHome());
        view.findViewById(R.id.registerRedirectButton).setOnClickListener(v -> navigateToRegister());
        view.findViewById(R.id.buttonKT1).setOnClickListener(v -> navigateToProfile());

        return view;
    }

    /*
    DEPREDACTED - use navbar instead
    private void navigateToHome() {
        UserHomeFragment userHomeFragment = new UserHomeFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, userHomeFragment)
                .addToBackStack(null)
                .commit();
    }*/

    private void navigateToRegister() {
        RegisterFragment registerFragment = new RegisterFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, registerFragment)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToProfile() {
        ProfileInfoFragment profileFragment = new ProfileInfoFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, profileFragment)
                .addToBackStack(null)
                .commit();
    }
}
