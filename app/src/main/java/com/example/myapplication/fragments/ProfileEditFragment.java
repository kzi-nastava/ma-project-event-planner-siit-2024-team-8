package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.domain.dto.ProviderInfoResponse;
import com.example.myapplication.domain.dto.UpdateUserRequest;
import com.example.myapplication.domain.dto.UserInfoResponse;
import com.example.myapplication.domain.enumerations.Role;
import com.example.myapplication.services.UserService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.viewmodels.UserViewModel;

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

    private ProviderInfoResponse providerInfo;
    UserService userService = new UserService();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserViewModel userVM;

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    public ProfileEditFragment(UserInfoResponse userInfo,ProviderInfoResponse providerInfo) {
        this.userInfo = userInfo;
        this.providerInfo = providerInfo;
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
        userVM = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
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
            name.setText(this.userInfo.firstName);
            lastName.setText(this.userInfo.lastName);
            address.setText(this.userInfo.address);
            phone.setText(this.userInfo.number);
        } else {
            EditText companyName = view.findViewById(R.id.companyNameEditText);
            EditText companyDesc =view.findViewById(R.id.companyDescEditText);
            name.setText(this.providerInfo.firstName);
            lastName.setText(this.providerInfo.lastName);
            address.setText(this.providerInfo.address);
            phone.setText(this.providerInfo.number);
            companyName.setText(this.providerInfo.companyName);
            companyDesc.setText(this.providerInfo.companyDescription);
        }


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

        RequestBody companyName = null;
        RequestBody companyDesc = null;

        if (JwtTokenUtil.getRole() == Role.PROVIDER){
            EditText companyNameEdit = getView().findViewById(R.id.companyNameEditText);
            companyName = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(companyNameEdit.getText()));

            EditText companyDescEdit = getView().findViewById(R.id.companyDescEditText);
            companyDesc = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(companyDescEdit.getText()));
        }

        // Handle image upload if available
        File imageFile = null;  // Set your image file here (if any)
        MultipartBody.Part imagePart = null;
        if (imageFile != null) {
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequestBody);
        }

        userVM.editUser(firstName,lastName,email,address,number,companyName,companyDesc,imagePart,getContext());

        getParentFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}