package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.AssetCardAdapter;
import com.example.eventplanner.adapters.EventCardAdapter;
import com.example.eventplanner.domain.OfferingType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OfferingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferingsFragment<T,Z> extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OfferingType type;

    public void setType(OfferingType type) {
        this.type = type;
    }
    public OfferingsFragment(){}
    public OfferingsFragment(OfferingType type){
        this.type = type;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssetsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        RecyclerView assetRecyclerView = view.findViewById(R.id.assetRecyclerView);
        TextView header = view.findViewById(R.id.textAssetHeader);
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        switch (type) {
            case EVENT:
                EventCardAdapter eventAdapter = new EventCardAdapter(view.getContext());
                eventAdapter.set_eventCards(UserHomeFragment.createEvents());
                assetRecyclerView.setAdapter(eventAdapter);
                header.setText("All Events");
                break;
            case ASSET:
                AssetCardAdapter adapter = new AssetCardAdapter(view.getContext());
                adapter.setAssets(UserHomeFragment.createAssets());
                assetRecyclerView.setAdapter(adapter);
                header.setText("All Assets");
                break;
            default:

        }
    }
}