package com.example.myapplication.fragments.event.event_info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AssetButtonAdapter;
import com.example.myapplication.domain.dto.user.AssetResponse;
import com.example.myapplication.domain.enumerations.AssetType;
import com.example.myapplication.fragments.asset.AssetInfoFragment;
import com.example.myapplication.viewmodels.AssetViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoughtAssetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoughtAssetsFragment extends DialogFragment implements AssetButtonAdapter.OnAssetClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String versionIds;

    private RecyclerView assetRecyclerView;

    private AssetButtonAdapter adapter;

    private AssetViewModel assetVM;

    private List<String> boughtVersionIds = new ArrayList<>();
    private String assetCategory;

    private String eventId;

    public BoughtAssetsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment BoughtAssetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoughtAssetsFragment newInstance(String versionIds, String assetCategory,String eventId) {
        BoughtAssetsFragment fragment = new BoughtAssetsFragment();
        Bundle args = new Bundle();
        args.putString("versionIds", versionIds);
        args.putString("asset_category", assetCategory);
        args.putString("eventId",eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            versionIds = getArguments().getString("versionIds");
            Type listType = new TypeToken<List<String>>(){}.getType();
            boughtVersionIds = new Gson().fromJson(versionIds, listType);

            assetCategory = getArguments().getString("asset_category");
            eventId = getArguments().getString("eventId");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Make dialog width match parent
            int widthInDp = 350;
            float scale = getResources().getDisplayMetrics().density;
            int widthInPx = (int) (widthInDp * scale + 0.5f);


            getDialog().getWindow().setLayout(
                    widthInPx,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bought_assets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assetVM = new ViewModelProvider(requireActivity()).get(AssetViewModel.class);
        assetVM.getBoughtAssets().observe(getViewLifecycleOwner(),assetResponses -> {
            adapter.updateData(assetResponses);
        });
        assetVM.fetchBoughtAssets(this.boughtVersionIds);
        TextView assetCategoryTV = view.findViewById(R.id.assetCategoryTV);
        assetCategoryTV.setText(assetCategory);
        setupRecyclerView(view);
    }

    private void setupRecyclerView(View view) {
        assetRecyclerView = view.findViewById(R.id.assetsRecyclerView);
        adapter = new AssetButtonAdapter(this);
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        assetRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onAssetClick(AssetResponse assetResponse) {
        AssetType type = assetResponse.getType();
        AssetInfoFragment assetInfoFragment = AssetInfoFragment.newInstance(assetResponse.getId().toString(),type.toString(),false,eventId);
        getParentFragment().getParentFragmentManager().beginTransaction().replace(R.id.main,assetInfoFragment).addToBackStack(null).commit();
    }
}