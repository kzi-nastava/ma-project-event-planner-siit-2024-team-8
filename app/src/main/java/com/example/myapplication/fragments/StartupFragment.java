package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.fragments.register.RegisterFragment;
import com.example.myapplication.fragments.user.LoginFragment;
import com.example.myapplication.fragments.user.ProfileInfoFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StartupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartupFragment newInstance(String param1, String param2) {
        StartupFragment fragment = new StartupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_startup, container, false);

        Button register = view.findViewById(R.id.registerRedirectButton);
        register.setOnClickListener(v -> openRegisterFragment());
        
        Button login = view.findViewById(R.id.signInButton);
        login.setOnClickListener(v -> openSignInFragment());

        TextView guestTextView = view.findViewById(R.id.guestTextView);
        guestTextView.setOnClickListener(v -> onGuestTextViewClicked());

        Button kt1 = view.findViewById(R.id.buttonKT1);
        kt1.setOnClickListener(v -> openProfileFragment());
        return view;
    }

    private void onGuestTextViewClicked() {
        HomePageFragment homePageFragment = new HomePageFragment();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main,homePageFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openSignInFragment() {
        LoginFragment fragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main,fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openRegisterFragment() {
        RegisterFragment fragment = new RegisterFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main,fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openProfileFragment() {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main,fragment)
                .addToBackStack(null)
                .commit();
    }
}