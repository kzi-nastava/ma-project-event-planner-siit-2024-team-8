package com.example.myapplication.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.myapplication.R;
import com.example.myapplication.adapters.BlockedUsersAdapter;
import com.example.myapplication.services.UserService;
import com.example.myapplication.viewmodels.UserViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlockedUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlockedUsersFragment extends DialogFragment implements BlockedUsersAdapter.UnblockListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView blockedRecyclerView;

    private BlockedUsersAdapter adapter;

    private UserViewModel userVM;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlockedUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlockedUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlockedUsersFragment newInstance(String param1, String param2) {
        BlockedUsersFragment fragment = new BlockedUsersFragment();
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
        return inflater.inflate(R.layout.fragment_blocked_users, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        int widthDp = 300;   // your desired width in dp
        int heightDp = 400;  // your desired height in dp

        float scale = getResources().getDisplayMetrics().density;
        int widthPx = (int) (widthDp * scale + 0.5f);
        int heightPx = (int) (heightDp * scale + 0.5f);
        window.setLayout(widthPx,heightPx);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userVM = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userVM.fetchBlockedUsers();
        setupRecyclerView(view);
        userVM.getBlockedUsers().observe(getViewLifecycleOwner(), blockedUserResponses -> {
            adapter.setBlocked(blockedUserResponses);
            if(blockedUserResponses.isEmpty()){
                view.findViewById(R.id.noBlockedUsersTV).setVisibility(View.VISIBLE);
            }else{
                view.findViewById(R.id.noBlockedUsersTV).setVisibility(View.GONE);
            }
        });

    }

    private void setupRecyclerView(View view) {
        blockedRecyclerView = view.findViewById(R.id.blockedUsersRV);
        blockedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BlockedUsersAdapter(this);
        blockedRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onUnblockClicked(List<String> blockedIds) {
        UserService userService = new UserService();
        userService.updateBlockedUsers(blockedIds,getContext());
    }
}