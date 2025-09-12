package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.auth0.android.jwt.JWT;
import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.adapters.AssetCardAdapter;
import com.example.myapplication.adapters.EventCardAdapter;
import com.example.myapplication.domain.enumerations.OfferingType;
import com.example.myapplication.fragments.asset.AssetCategoriesFragment;
import com.example.myapplication.fragments.user.LoginFragment;
import com.example.myapplication.fragments.user.ProfileInfoFragment;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.viewmodels.AssetViewModel;
import com.example.myapplication.viewmodels.EventViewModel;

public class HomePageFragment extends Fragment {

    private RecyclerView eventRecyclerView;
    private RecyclerView assetRecyclerView;
    private AssetViewModel assetViewModel;

    private EventViewModel eventViewModel;
    private AssetCardAdapter assetCardAdapter;

    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance() {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetViewModel = new ViewModelProvider(this).get(AssetViewModel.class);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("HomePageFragment", "created view");
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        if (JwtTokenUtil.isUserLoggedIn()) {
            TextView role = view.findViewById(R.id.roleTextView);
            role.setText(JwtTokenUtil.getRole().toString());
        }
        return view;
    }

    public void onProfileClick(View view) {
        if (isUserLoggedIn(view.getContext())) {
            ProfileInfoFragment userProfileFragment = new ProfileInfoFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, userProfileFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            LoginFragment loginFragment = new LoginFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, loginFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    private boolean isUserLoggedIn(Context context) {
        try {
            if (JwtTokenUtil.getToken() == null) {
                return false;
            }

            String jwtToken = JwtTokenUtil.getToken();
            if (jwtToken != null) {
                JWT token = new JWT(jwtToken);
                return !token.isExpired(10);
            }
            return false;
        } catch (Exception e) {
            Log.e("JwtTokenUtil", "Error checking login state", e);
            return false;
        }
    }

    private void onSeeAllClick(OfferingType type) {
        AllSolutionsFragment fragment = new AllSolutionsFragment(type);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main,fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assetViewModel.loadTopAssets();
        assetRecyclerView = view.findViewById(R.id.assetRecyclerView);
        assetCardAdapter = new AssetCardAdapter(getContext());
        assetRecyclerView.setAdapter(assetCardAdapter);
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        assetCardAdapter.SetOnClick(getActivity(), getActivity().getSupportFragmentManager());
        assetViewModel.getTop5Assets().observe(getViewLifecycleOwner(), assets -> {
            if (assets != null) {
                assetCardAdapter.setAssets(assets);
            }
        });



        String token = JwtTokenUtil.getToken();
        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;
            assetViewModel.fetchAssets(authHeader);
        }

        eventViewModel.fetchTop5Events();
        eventRecyclerView = view.findViewById(R.id.eventRecyclerView);
        EventCardAdapter eventCardAdapter = new EventCardAdapter(getContext());
        eventCardAdapter.SetOnClick((MainActivity) getActivity(), getActivity().getSupportFragmentManager());
        eventRecyclerView.setAdapter(eventCardAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        eventViewModel.getTop5().observe(getViewLifecycleOwner(),eventCardResponses -> {
            if(eventCardResponses!=null){
                eventCardAdapter.setEvents(eventCardResponses);
            }
        });

        setupClickListeners(view);
    }

    private void setupClickListeners(View view) {
        Button seeAllEvents = view.findViewById(R.id.seeAllEventsButton);
        Button seeAllAssets = view.findViewById(R.id.seeAllAssetsButton);
        ImageButton loginButton = view.findViewById(R.id.profileImageButton);

        seeAllEvents.setOnClickListener(v -> onSeeAllClick(OfferingType.EVENT));
        seeAllAssets.setOnClickListener(v -> onSeeAllClick(OfferingType.ASSET));
        loginButton.setOnClickListener(v -> onProfileClick(view));
    }

    private void navigateToAssetCategories() {
        AssetCategoriesFragment assetCategoriesFragment = new AssetCategoriesFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, assetCategoriesFragment)
                .addToBackStack(null)
                .commit();
    }
}