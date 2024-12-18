package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.asset.AssetDetailsFragment;
import com.example.myapplication.fragments.asset.AssetOverviewFragment;
import com.example.myapplication.fragments.ToDoFragment;

public class AssetViewPagerAdapter extends FragmentStateAdapter {

    public AssetViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AssetOverviewFragment();
            case 1:
                return new ToDoFragment();
            case 2:
                return new AssetDetailsFragment();
            default:
                throw new IllegalArgumentException("Invalid position");
        }
    }


    @Override
    public int getItemCount() {
        return 3; // Total number of fragments
    }
}