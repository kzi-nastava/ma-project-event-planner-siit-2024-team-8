package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.domain.dto.UpdateUserRequest;
import com.example.myapplication.domain.dto.UserInfoResponse;
import com.example.myapplication.services.UserService;
import com.example.myapplication.utilities.JwtTokenUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileEditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private UserInfoResponse userInfo;
    UserService userService = new UserService();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    public ProfileEditFragment(UserInfoResponse userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileEditFragment newInstance(String param1, String param2) {
        ProfileEditFragment fragment = new ProfileEditFragment();
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

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        EditText name = view.findViewById(R.id.nameEditText);
        EditText lastName = view.findViewById(R.id.lastEditText);
        EditText address = view.findViewById(R.id.addressEditText);
        EditText phone = view.findViewById(R.id.phoneEditText);
        if (!String.valueOf(JwtTokenUtil.getRole()).equalsIgnoreCase("PROVIDER")) {
            view.findViewById(R.id.companyNameEditView1).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.companyNameEditView2).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.companyNameEditText).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.companyDescEditView1).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.companyDescEditView2).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.companyDescEditText).setVisibility(View.INVISIBLE);
        } else {
            EditText companyName = view.findViewById(R.id.companyNameEditText);
            EditText companyDesc =view.findViewById(R.id.companyDescEditText);
            /*
            !!! UNAVAILABLE UNTIL PROVIDER IS FIXED !!!
            companyName.setText(this.userInfo.companyName);
            companyDesc.setText(this.userInfo.companyDesc);
            */
        }
        name.setText(this.userInfo.firstName);
        lastName.setText(this.userInfo.lastName);
        address.setText(this.userInfo.address);
        phone.setText(this.userInfo.number);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button updateButton = view.findViewById(R.id.updateSubmitButton);
        Button cancelButton = view.findViewById(R.id.updateCancelButton);

        updateButton.setOnClickListener(v -> submitClicked());
        cancelButton.setOnClickListener(v -> cancelClicked());
    }

    public void cancelClicked() {
        replaceFragment(new ProfileInfoFragment());
    }

    public void submitClicked() {
        // Prepare data to send as form fields
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), userInfo.email);
        EditText nameEdit = getView().findViewById(R.id.nameEditText);
        RequestBody firstName = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(nameEdit.getText()));

        EditText surnameEdit = getView().findViewById(R.id.lastEditText);
        RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(surnameEdit.getText()));

        EditText addressEdit = getView().findViewById(R.id.addressEditText);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(addressEdit.getText()));

        EditText phoneEdit = getView().findViewById(R.id.phoneEditText);
        RequestBody number = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(phoneEdit.getText()));

        // Handle image upload if available
        File imageFile = null;  // Set your image file here (if any)
        MultipartBody.Part imagePart = null;
        if (imageFile != null) {
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequestBody);
        }

        // Send data to backend
        userService.getApiService().updateUser(firstName, lastName, email, address, number, imagePart).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "User updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to update user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        replaceFragment(new HomePageFragment());
    }

}