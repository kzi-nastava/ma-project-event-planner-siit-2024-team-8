package com.example.eventplanner.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventplanner.R;
import com.example.eventplanner.domain.OfferingType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssetFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AssetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssetInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssetFragment newInstance(String param1, String param2) {
        AssetFragment fragment = new AssetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AssetFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_asset, container, false);

        Button editButton = view.findViewById(R.id.edit_button);
        Button backButton = view.findViewById(R.id.back_button);

        // Set click listeners for buttons
        editButton.setOnClickListener(v -> openAssetEditFragment());
        backButton.setOnClickListener(v -> openOfferingsFragment());

        return view;
    }

    private void openAssetEditFragment() {
        AssetEditFragment assetEditFragment = new AssetEditFragment();

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, assetEditFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openOfferingsFragment() {
        OfferingsFragment offeringsFragment = new OfferingsFragment();
        offeringsFragment.setType(OfferingType.ASSET);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, offeringsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}