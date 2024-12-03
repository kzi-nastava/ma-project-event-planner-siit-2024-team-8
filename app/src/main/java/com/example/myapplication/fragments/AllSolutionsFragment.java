package com.example.myapplication.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AssetCardAdapter;
import com.example.myapplication.adapters.EventCardAdapter;
import com.example.myapplication.domain.OfferingType;
import com.example.myapplication.viewmodels.EventViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class AllSolutionsFragment extends Fragment {

    private EventViewModel mViewModel;

    private OfferingType type;

    public void setType(OfferingType type) {
        this.type = type;
    }

    public AllSolutionsFragment() {}

    public AllSolutionsFragment(OfferingType type) {
        this.type = type;
    }

    public static AllSolutionsFragment newInstance(){
        return null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_solutions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        RecyclerView assetRecyclerView = view.findViewById(R.id.soultionsRecyclerView);
        TextView header = view.findViewById(R.id.titleSolution);
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        switch (type) {
            case EVENT:
                EventCardAdapter eventAdapter = new EventCardAdapter(view.getContext());
                eventAdapter.SetOnClick(getActivity(),getActivity().getSupportFragmentManager());
                eventAdapter.set_eventCards(HomePageFragment.createEvents());
                assetRecyclerView.setAdapter(eventAdapter);
                header.setText("Events");
                break;
            case ASSET:
                AssetCardAdapter assetAdapter = new AssetCardAdapter(view.getContext());
                assetAdapter.setAssets(HomePageFragment.createAssets());
                assetAdapter.SetOnClick(getActivity(),getActivity().getSupportFragmentManager());

                assetRecyclerView.setAdapter(assetAdapter);
                header.setText("Assets");
                break;
            default:
                break;
        }
    }


}