package com.example.eventplanner.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventplanner.R;
import com.example.eventplanner.domain.OfferingType;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileInfoFragment extends Fragment {

    private TextView userName, userEmail, userFullName, userAddress, userPhone, accountStatus, companyName, companyDescription;
    private ShapeableImageView profilePicture;
    private Button editButton, myAssetsButton, createAssetButton;

    private String mUserName, mUserEmail, mUserFullName, mUserAddress, mUserPhone, mAccountStatus, mCompanyName, mCompanyDescription;

    public ProfileInfoFragment() {
    }



    public static ProfileInfoFragment newInstance(String userName, String userEmail, String userFullName, String userAddress,
                                                  String userPhone, String accountStatus, String companyName, String companyDescription) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        Bundle args = new Bundle();
        args.putString("USER_NAME", userName);
        args.putString("USER_EMAIL", userEmail);
        args.putString("USER_FULL_NAME", userFullName);
        args.putString("USER_ADDRESS", userAddress);
        args.putString("USER_PHONE", userPhone);
        args.putString("ACCOUNT_STATUS", accountStatus);
        args.putString("COMPANY_NAME", companyName);
        args.putString("COMPANY_DESCRIPTION", companyDescription);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserName = getArguments().getString("USER_NAME");
            mUserEmail = getArguments().getString("USER_EMAIL");
            mUserFullName = getArguments().getString("USER_FULL_NAME");
            mUserAddress = getArguments().getString("USER_ADDRESS");
            mUserPhone = getArguments().getString("USER_PHONE");
            mAccountStatus = getArguments().getString("ACCOUNT_STATUS");
            mCompanyName = getArguments().getString("COMPANY_NAME");
            mCompanyDescription = getArguments().getString("COMPANY_DESCRIPTION");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_info, container, false);

        if (getArguments() != null) {
            mUserName = getArguments().getString("USER_NAME");
            mUserEmail = getArguments().getString("USER_EMAIL");
            mUserFullName = getArguments().getString("USER_FULL_NAME");
            mUserAddress = getArguments().getString("USER_ADDRESS");
            mUserPhone = getArguments().getString("USER_PHONE");
            mAccountStatus = getArguments().getString("ACCOUNT_STATUS");
            mCompanyName = getArguments().getString("COMPANY_NAME");
            mCompanyDescription = getArguments().getString("COMPANY_DESCRIPTION");
        }

        if (mUserName == null || mUserEmail == null || mUserFullName == null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userProfile", getActivity().MODE_PRIVATE);
            mUserName = sharedPreferences.getString("USER_NAME", "Default Name");
            mUserEmail = sharedPreferences.getString("USER_EMAIL", "Default Email");
            mUserFullName = sharedPreferences.getString("USER_FULL_NAME", "Default Full Name");
            mUserAddress = sharedPreferences.getString("USER_ADDRESS", "Default Address");
            mUserPhone = sharedPreferences.getString("USER_PHONE", "Default Phone");
            mAccountStatus = sharedPreferences.getString("ACCOUNT_STATUS", "Default Status");
            mCompanyName = sharedPreferences.getString("COMPANY_NAME", "Default Company");
            mCompanyDescription = sharedPreferences.getString("COMPANY_DESCRIPTION", "Default Description");
        }

        userName = rootView.findViewById(R.id.user_name);
        userEmail = rootView.findViewById(R.id.user_email);
        userFullName = rootView.findViewById(R.id.user_fullname);
        userAddress = rootView.findViewById(R.id.user_address);
        userPhone = rootView.findViewById(R.id.user_phone);
        accountStatus = rootView.findViewById(R.id.account_status);
        companyName = rootView.findViewById(R.id.company_name);
        companyDescription = rootView.findViewById(R.id.company_description);
        profilePicture = rootView.findViewById(R.id.profile_picture);
        editButton = rootView.findViewById(R.id.edit_button);
        myAssetsButton = rootView.findViewById(R.id.my_assets_button);
        createAssetButton = rootView.findViewById(R.id.create_asset_button);

        // Set the data to the views
        userName.setText(mUserName);
        userEmail.setText(mUserEmail);
        userFullName.setText(mUserFullName);
        userAddress.setText(mUserAddress);
        userPhone.setText(mUserPhone);
        accountStatus.setText(mAccountStatus);
        companyName.setText(mCompanyName);
        companyDescription.setText(mCompanyDescription);

        // Handle button clicks as before
        editButton.setOnClickListener(v -> openProfileInfoEditFragment());
        myAssetsButton.setOnClickListener(v -> openOfferingsFragment());
        createAssetButton.setOnClickListener(v -> openAssetCreateFragment());


        return rootView;
    }

    private void openProfileInfoEditFragment() {

        ProfileInfoEditFragment profileInfoEditFragment = new ProfileInfoEditFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.profile_fragment_container, profileInfoEditFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openOfferingsFragment() {
        OfferingsFragment offeringsFragment = new OfferingsFragment();
        offeringsFragment.setType(OfferingType.ASSET);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.profile_fragment_container, offeringsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openAssetCreateFragment() {
        AssetCreateFragment assetCreateFragment = new AssetCreateFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.profile_fragment_container, assetCreateFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
