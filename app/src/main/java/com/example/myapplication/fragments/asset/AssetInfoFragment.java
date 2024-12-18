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

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        AssetViewPagerAdapter adapter = new AssetViewPagerAdapter(requireActivity(), assetId, assetType);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(true);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView2);
        //NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.assetInfoFragmentLayout);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.overviewFragment) {
                viewPager.setCurrentItem(0, true);
            } else if (itemId == R.id.budgetFragment) {
                viewPager.setCurrentItem(1, true);
            } else if (itemId == R.id.providerInfoFragment) {
                viewPager.setCurrentItem(2, true);
            }
            return true;
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.overviewFragment);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.buyFragment);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.organizerInfoFragment);
                        break;
                }
            }
        });

        /*
        if (navHostFragment != null && bottomNavigationView!=null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
         */

        return view;
    }

}