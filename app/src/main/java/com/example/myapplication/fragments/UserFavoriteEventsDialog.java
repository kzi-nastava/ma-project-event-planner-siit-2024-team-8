package com.example.myapplication.fragments;

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

import com.example.myapplication.R;
import com.example.myapplication.adapters.EventButtonAdapter;
import com.example.myapplication.adapters.EventCardAdapter;
import com.example.myapplication.domain.dto.event.EventCardResponse;
import com.example.myapplication.fragments.event.event_info.EventInfoFragment;
import com.example.myapplication.viewmodels.EventViewModel;
import com.example.myapplication.viewmodels.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFavoriteEventsDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFavoriteEventsDialog extends DialogFragment implements EventCardAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView eventsRecyclerView;

    private EventButtonAdapter adapter;
    private UserViewModel userVM;

    public UserFavoriteEventsDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFavoriteEventsDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFavoriteEventsDialog newInstance(String param1, String param2) {
        UserFavoriteEventsDialog fragment = new UserFavoriteEventsDialog();
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
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.white);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_favorite_events_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView(view);
        userVM = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userVM.getFavoriteEvents().observe(getViewLifecycleOwner(), eventInfoResponses -> {
            adapter.updateData(eventInfoResponses);
        });
        userVM.fetchFavoriteEvents();
    }

    private void setupRecyclerView(View view) {
        eventsRecyclerView = view.findViewById(R.id.favEventsRecyclerView);
        adapter = new EventButtonAdapter(this);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(EventCardResponse event) {
        EventInfoFragment eventInfoFragment = EventInfoFragment.newInstance(event.getId().toString());
        getParentFragmentManager().beginTransaction().replace(R.id.main,eventInfoFragment).addToBackStack(null).commit();
        dismiss();
    }
}