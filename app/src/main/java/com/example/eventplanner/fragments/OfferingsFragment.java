package com.example.eventplanner.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.AssetCardAdapter;
import com.example.eventplanner.adapters.EventCardAdapter;
import com.example.eventplanner.domain.OfferingType;
import com.example.eventplanner.domain.AssetDTO;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class OfferingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OfferingType type;

    public void setType(OfferingType type) {
        this.type = type;
    }

    public OfferingsFragment() {}

    public OfferingsFragment(OfferingType type) {
        this.type = type;
    }

    public static OfferingsFragment newInstance(String param1, String param2) {
        OfferingsFragment fragment = new OfferingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offerings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        RecyclerView assetRecyclerView = view.findViewById(R.id.offeringsRecyclerView);
        TextView header = view.findViewById(R.id.textAssetHeader);
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        switch (type) {
            case EVENT:
                EventCardAdapter eventAdapter = new EventCardAdapter(view.getContext());
                eventAdapter.set_eventCards(UserHomeFragment.createEvents());
                assetRecyclerView.setAdapter(eventAdapter);
                header.setText("Search Events");
                break;
            case ASSET:
                AssetCardAdapter assetAdapter = new AssetCardAdapter(view.getContext());
                assetAdapter.setAssets(UserHomeFragment.createAssets());

                assetAdapter.setItemClickListener(new AssetCardAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(AssetDTO asset) {
                        Log.d("OfferingsFragment", "Clicked on asset: " + asset.getName());
                        AssetFragment assetFragment = AssetFragment.newInstance(asset.getName(), asset.getType().toString());
                        if (getActivity() != null) {
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_layout, assetFragment)
                                    .addToBackStack(null)  // Add to backstack so you can go back
                                    .commit();
                        }
                    }
                });

                assetRecyclerView.setAdapter(assetAdapter);
                header.setText("Search Assets");
                break;
            default:
                break;
        }

        SwitchMaterial material = view.findViewById(R.id.orderSwitch);
        material.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    material.setText("DESCENDING");
                } else {
                    material.setText("ASCENDING");
                }
            }
        });
    }
}
