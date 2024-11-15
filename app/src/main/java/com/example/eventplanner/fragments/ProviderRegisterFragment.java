package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.eventplanner.R;

public class ProviderRegisterFragment extends Fragment {

    private EditText descriptionEditText, companyNameEditText;
    private ImageButton companyImageButton;
    private Button registerProviderButton, backToRegisterButton;

    public ProviderRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_register, container, false);

        descriptionEditText = view.findViewById(R.id.editTextTextMultiLine);
        companyNameEditText = view.findViewById(R.id.editTextText11);
        companyImageButton = view.findViewById(R.id.imageButton2);
        registerProviderButton = view.findViewById(R.id.registerProviderButton);
        backToRegisterButton = view.findViewById(R.id.backToRegisterButton);

        backToRegisterButton.setOnClickListener(v -> {
            RegisterFragment registerFragment = new RegisterFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_layout, registerFragment)
                    .addToBackStack(null)
                    .commit();
        });

        registerProviderButton.setOnClickListener(v -> {
        });

        return view;
    }
}
