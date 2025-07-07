package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.asset.AssetDetailsFragment;
import com.example.myapplication.fragments.asset.AssetInfoFragment;
import com.example.myapplication.fragments.asset.AssetOverviewFragment;
import com.example.myapplication.fragments.asset.EditAssetFragment;

public class AssetViewPagerAdapter extends FragmentStateAdapter {

    private final String assetId;
    private final String assetType;

    private final boolean isProvider;

    public AssetViewPagerAdapter(@NonNull AssetInfoFragment fragmentActivity, String assetId, String assetType, boolean isProvider) {
        super(fragmentActivity);
        this.assetId = assetId;
        this.assetType = assetType;
        this.isProvider = isProvider;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (isProvider) {
            switch (position) {
                case 0:
                    return AssetOverviewFragment.newInstance(assetId, assetType);
                case 1:
                    return AssetDetailsFragment.newInstance(assetId, assetType);
                default:
                    throw new IllegalArgumentException("Invalid position");
            }
        } else {
        return AssetOverviewFragment.newInstance(assetId, assetType);
    }
    }

    @Override
    public int getItemCount() {
        return isProvider ? 2 : 1; // Total number of fragments
    }
}