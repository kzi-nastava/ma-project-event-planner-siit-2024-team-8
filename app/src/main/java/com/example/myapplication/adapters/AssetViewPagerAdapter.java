package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.asset.AssetDetailsFragment;
import com.example.myapplication.fragments.asset.AssetOverviewFragment;
import com.example.myapplication.fragments.ToDoFragment;

public class AssetViewPagerAdapter extends FragmentStateAdapter {

    private final String assetId;
    private final String assetType;

    public AssetViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String assetId, String assetType) {
        super(fragmentActivity);
        this.assetId = assetId;
        this.assetType = assetType;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return AssetOverviewFragment.newInstance(assetId, assetType);
            case 1:
                return new ToDoFragment();
            case 2:
                return AssetDetailsFragment.newInstance(assetId, assetType);
            default:
                throw new IllegalArgumentException("Invalid position");
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Total number of fragments
    }
}