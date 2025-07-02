package com.example.myapplication.fragments.asset;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AssetViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssetInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssetInfoFragment extends Fragment {

    private String assetId;
    private String assetType;

    public AssetInfoFragment() {
        // Required empty public constructor
    }

    public static AssetInfoFragment newInstance(String assetId, String assetType) {
        AssetInfoFragment fragment = new AssetInfoFragment();
        Bundle args = new Bundle();
        args.putString("asset_id", assetId);
        args.putString("asset_type", assetType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assetId = getArguments().getString("asset_id");
            assetType = getArguments().getString("asset_type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_asset_info, container, false);
        return view;
    }

}