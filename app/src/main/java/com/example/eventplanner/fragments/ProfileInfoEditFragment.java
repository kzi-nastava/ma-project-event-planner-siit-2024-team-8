package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileInfoEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileInfoEditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileInfoEditFragment() {
    }

    public static ProfileInfoEditFragment newInstance(String param1, String param2) {
        ProfileInfoEditFragment fragment = new ProfileInfoEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_info_edit, container, false);

        rootView.findViewById(R.id.cancel_button).setOnClickListener(v -> onClickCancelButton());

        return rootView;
    }

    private void onClickCancelButton() {
        ProfileInfoFragment profileInfoFragment = new ProfileInfoFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, profileInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}