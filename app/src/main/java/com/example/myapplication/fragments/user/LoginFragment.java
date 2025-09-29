package com.example.myapplication.fragments.user;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.domain.AuthResponse;
import com.example.myapplication.domain.enumerations.Role;
import com.example.myapplication.domain.dto.LoginRequest;
import com.example.myapplication.fragments.register.RegisterFragment;
import com.example.myapplication.services.ClientUtils;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.utilities.NotificationsUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String email;
    private String password;

    public LoginFragment() {
        // Required empty public constructor
    }

    public interface OnRoleChangeListener {
        void onRoleChanged(Role role);
    }

    private OnRoleChangeListener roleChangeListener;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        // Ensure the activity implements the listener interface
        if (context instanceof OnRoleChangeListener) {
            roleChangeListener = (OnRoleChangeListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnRoleChangeListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button registerRedirect = view.findViewById(R.id.registerRedirectButton3);
        registerRedirect.setOnClickListener(v -> onClickRegister());

        Button loginButton = view.findViewById(R.id.signInButton);
        loginButton.setOnClickListener( v -> onClickLogin(view));

        return view;
    }

    public void notifyRoleChanged(Role role) {
        if (roleChangeListener != null) {
            roleChangeListener.onRoleChanged(role);
        }
    }
    public void onClickLogin(View view) {
        EditText emailET = view.findViewById(R.id.editTextEmail);
        email = String.valueOf(emailET.getText());
        EditText passwordEditText = view.findViewById(R.id.editTextPassword);
        password = String.valueOf(passwordEditText.getText());

        LoginRequest loginRequest = new LoginRequest(email, password);
        ClientUtils.loginService.login(loginRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    String jwtToken = response.body().getToken();
                    String id = response.body().getId();
                    try {
                        JwtTokenUtil.saveToken(jwtToken,getContext());
                        JwtTokenUtil.saveUserId(id,getContext());
                        notifyRoleChanged(JwtTokenUtil.getRole());
                        NotificationsUtils.getInstance().showSuccessToast(requireContext(),"Successfully logged in!");
                    } catch (GeneralSecurityException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }else if (response.code() == 404){
                    NotificationsUtils.getInstance().showErrToast(requireContext(),"User not found!\nEmail or password are invalid!");
                }else if (response.code() == 403){
                        NotificationsUtils.getInstance().showErrToast(requireContext(), "Forbidden");
                }else if(response.code() == 400){
                    if (response.errorBody() != null) {
                        String errorJson = null;
                        try {
                            errorJson = response.errorBody().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        // Parse errorJson into your AuthResponse or a generic error message
                        AuthResponse errorResponse = new Gson().fromJson(errorJson, AuthResponse.class);
                        if (errorResponse != null) {
                            NotificationsUtils.getInstance().showErrToast(requireContext(), errorResponse.getMessage());
                        } else {
                            NotificationsUtils.getInstance().showErrToast(requireContext(), "Accout has not been activated");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("LoginError", "Request failed", t); // logs full stack trace
                NotificationsUtils.getInstance().showErrToast(requireContext(), "Login failed: " + t.getMessage());
            }
        });
    }
    public void onClickRegister() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .detach(this)
                .commit();
        RegisterFragment fragment = new RegisterFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }
}