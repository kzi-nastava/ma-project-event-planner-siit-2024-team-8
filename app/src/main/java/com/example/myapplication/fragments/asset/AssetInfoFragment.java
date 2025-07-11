package com.example.myapplication.fragments.asset;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AssetViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssetInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssetInfoFragment extends Fragment {

    private String assetId;
    private String assetType;
    private boolean isProvider;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    public AssetInfoFragment() { }

    public static AssetInfoFragment newInstance(String assetId, String assetType, boolean isProvider) {
        AssetInfoFragment fragment = new AssetInfoFragment();
        Bundle args = new Bundle();
        args.putString("asset_id", assetId);
        args.putString("asset_type", assetType);
        args.putBoolean("is_provider", isProvider);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assetId = getArguments().getString("asset_id");
            assetType = getArguments().getString("asset_type");
            isProvider = getArguments().getBoolean("is_provider", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_info, container, false);

        viewPager = view.findViewById(R.id.viewPagerAssetInfo);
        tabLayout = view.findViewById(R.id.tabLayoutAssetInfo);

        AssetViewPagerAdapter adapter = new AssetViewPagerAdapter(this, assetId, assetType, isProvider);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (isProvider) {
                switch (position) {
                    case 0: tab.setText("Overview"); break;
                    case 1: tab.setText("Details"); break;
                }
            } else {
                tab.setText("Overview");
            }
        }).attach();

        return view;
    }
}