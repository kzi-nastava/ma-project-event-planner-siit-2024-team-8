package com.example.myapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AssetCardAdapter;
import com.example.myapplication.adapters.EventCardAdapter;
import com.example.myapplication.domain.Asset;
import com.example.myapplication.domain.AssetType;
import com.example.myapplication.domain.Event;
import com.example.myapplication.domain.OfferingType;
import com.example.myapplication.fragments.asset.AssetCategoriesFragment;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.viewmodels.AssetViewModel;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HomePageFragment extends Fragment {

    private RecyclerView eventRecyclerView;
    private RecyclerView assetRecyclerView;
    private AssetViewModel assetViewModel;
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
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("HomePageFragment", "created view");
        return inflater.inflate(R.layout.fragment_home_page, container, false);

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
                return !token.isExpired(10); // Adjust the leeway (e.g., 10 seconds)
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

        assetRecyclerView = view.findViewById(R.id.assetRecyclerView);
        assetCardAdapter = new AssetCardAdapter(getContext());
        assetRecyclerView.setAdapter(assetCardAdapter);
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        assetCardAdapter.SetOnClick(getActivity(), getActivity().getSupportFragmentManager());

        assetViewModel.getAssetsLiveData().observe(getViewLifecycleOwner(), assets -> {
            if (assets != null) {
                assetCardAdapter.setAssets(assets.stream().limit(5).collect(Collectors.toList()));
            }
        });

        String token = JwtTokenUtil.getToken();
        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;
            assetViewModel.fetchAssets(authHeader);
        }

        SnapHelper snapHelper = new LinearSnapHelper();
        eventRecyclerView = view.findViewById(R.id.eventRecyclerView);
        EventCardAdapter eventCardAdapter = new EventCardAdapter(getContext());
        eventCardAdapter.SetOnClick(getActivity(), getActivity().getSupportFragmentManager());
        eventCardAdapter.set_eventCards(new ArrayList<>(createEvents().stream().limit(5).collect(Collectors.toList())));
        eventRecyclerView.setAdapter(eventCardAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        snapHelper.attachToRecyclerView(assetRecyclerView);

        setupClickListeners(view);
    }
    private void setupClickListeners(View view) {
        Button seeAllEvents = view.findViewById(R.id.seeAllEventsButton);
        Button seeAllAssets = view.findViewById(R.id.seeAllAssetsButton);
        ImageButton loginButton = view.findViewById(R.id.imageButton);
        MaterialButton categoryButton = view.findViewById(R.id.CategoryButton);

        seeAllEvents.setOnClickListener(v -> onSeeAllClick(OfferingType.EVENT));
        seeAllAssets.setOnClickListener(v -> onSeeAllClick(OfferingType.ASSET));
        categoryButton.setOnClickListener(v -> navigateToAssetCategories());
        loginButton.setOnClickListener(v -> onProfileClick(view));
    }

    private void navigateToAssetCategories() {
        AssetCategoriesFragment assetCategoriesFragment = new AssetCategoriesFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, assetCategoriesFragment)
                .addToBackStack(null)
                .commit();
    }



    public static ArrayList<Event> createEvents() {
        ArrayList<Event> eventCards = new ArrayList<>();
        eventCards.add(new Event("Exit", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://media.istockphoto.com/id/974238866/photo/audience-listens-to-the-lecturer-at-the-conference.jpg?s=612x612&w=0&k=20&c=p_BQCJWRQQtZYnQlOtZMzTjeB_csic8OofTCAKLwT0M="));
        eventCards.add(new Event("Conference", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZXZlbnR8ZW58MHx8MHx8fDA%3D"));
        eventCards.add(new Event("Exit", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://media.istockphoto.com/id/974238866/photo/audience-listens-to-the-lecturer-at-the-conference.jpg?s=612x612&w=0&k=20&c=p_BQCJWRQQtZYnQlOtZMzTjeB_csic8OofTCAKLwT0M="));
        eventCards.add(new Event("Conference", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZXZlbnR8ZW58MHx8MHx8fDA%3D"));
        eventCards.add(new Event("Exit", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://media.istockphoto.com/id/974238866/photo/audience-listens-to-the-lecturer-at-the-conference.jpg?s=612x612&w=0&k=20&c=p_BQCJWRQQtZYnQlOtZMzTjeB_csic8OofTCAKLwT0M="));
        eventCards.add(new Event("Conference", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZXZlbnR8ZW58MHx8MHx8fDA%3D"));
        eventCards.add(new Event("Exit", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://media.istockphoto.com/id/974238866/photo/audience-listens-to-the-lecturer-at-the-conference.jpg?s=612x612&w=0&k=20&c=p_BQCJWRQQtZYnQlOtZMzTjeB_csic8OofTCAKLwT0M="));
        eventCards.add(new Event("Conference", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZXZlbnR8ZW58MHx8MHx8fDA%3D"));
        eventCards.add(new Event("Exit", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://media.istockphoto.com/id/974238866/photo/audience-listens-to-the-lecturer-at-the-conference.jpg?s=612x612&w=0&k=20&c=p_BQCJWRQQtZYnQlOtZMzTjeB_csic8OofTCAKLwT0M="));
        eventCards.add(new Event("Conference", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZXZlbnR8ZW58MHx8MHx8fDA%3D"));
        eventCards.add(new Event("Exit", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://media.istockphoto.com/id/974238866/photo/audience-listens-to-the-lecturer-at-the-conference.jpg?s=612x612&w=0&k=20&c=p_BQCJWRQQtZYnQlOtZMzTjeB_csic8OofTCAKLwT0M="));
        eventCards.add(new Event("Conference", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZXZlbnR8ZW58MHx8MHx8fDA%3D"));
        eventCards.add(new Event("Exit", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://media.istockphoto.com/id/974238866/photo/audience-listens-to-the-lecturer-at-the-conference.jpg?s=612x612&w=0&k=20&c=p_BQCJWRQQtZYnQlOtZMzTjeB_csic8OofTCAKLwT0M="));
        eventCards.add(new Event("Conference", LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 14), "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZXZlbnR8ZW58MHx8MHx8fDA%3D"));

        return eventCards;
    }
}