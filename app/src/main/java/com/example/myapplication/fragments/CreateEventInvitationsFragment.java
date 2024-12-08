package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.adapters.InvitationsAdapter;
import com.example.myapplication.domain.Activity;
import com.example.myapplication.domain.Invitation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventInvitationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventInvitationsFragment extends Fragment implements InvitationsAdapter.OnNewInvitationsListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    private List<Invitation> invitations;

    private InvitationsAdapter invitationsAdapter;

    public CreateEventInvitationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventInvitationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventInvitationsFragment newInstance(String param1, String param2) {
        CreateEventInvitationsFragment fragment = new CreateEventInvitationsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_create_event_invitations, container, false);

        // Recycler view for activities
        recyclerView = view.findViewById(R.id.invitationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        invitations = new ArrayList<>();
        invitationsAdapter = new InvitationsAdapter(invitations,this);

        recyclerView.setAdapter(invitationsAdapter);

        Button addGuest = view.findViewById(R.id.addGuestButton);

        addGuest.setOnClickListener( v -> {
            onCreateNewInvitation();
        });

        return view;
    }

    @Override
    public void onCreateNewInvitation() {
        Invitation invitation = new Invitation(LocalDate.now(),"",true);
        invitations.add(invitation);
        invitationsAdapter.notifyItemInserted(invitations.size() - 1);
    }
}