package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.fragments.asset.AssetCategoriesFragment;
import com.example.myapplication.fragments.asset.CreateAssetFragment;
import com.google.android.material.button.MaterialButton;

public class ProfileInfoFragment extends Fragment {

    public ProfileInfoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton myAssetsButton = view.findViewById(R.id.my_assets_button);
        MaterialButton createAssetButton = view.findViewById(R.id.create_asset_button);
        MaterialButton assetCategoriesButton = view.findViewById(R.id.asset_categories_button);

        myAssetsButton.setOnClickListener(v -> replaceFragment(new AllSolutionsFragment()));
        createAssetButton.setOnClickListener(v -> replaceFragment(new CreateAssetFragment()));
        assetCategoriesButton.setOnClickListener(v -> replaceFragment(new AssetCategoriesFragment()));
    }

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }
}
