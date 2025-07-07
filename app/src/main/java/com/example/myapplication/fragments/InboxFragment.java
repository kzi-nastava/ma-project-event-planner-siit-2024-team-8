package com.example.myapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.InboxAdapter;
import com.example.myapplication.domain.InboxUser;
import com.example.myapplication.services.ChatAPIService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.utilities.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboxFragment extends Fragment {

    private RecyclerView inboxRecyclerView;
    private InboxAdapter adapter;
    private String currentUserId;
    private List<InboxUser> inboxUsers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        inboxRecyclerView = view.findViewById(R.id.inboxRecyclerView);
        inboxRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new InboxAdapter(inboxUsers, this::openChat);
        inboxRecyclerView.setAdapter(adapter);

        currentUserId = JwtTokenUtil.getUserId(); // Assuming you have this
        loadInboxUsers();

        return view;
    }

    private void loadInboxUsers() {
        RetrofitClient.getRetrofitInstance()
                .create(ChatAPIService.class)
                .getInboxUsers(currentUserId)
                .enqueue(new Callback<List<InboxUser>>() {
                    @Override
                    public void onResponse(Call<List<InboxUser>> call, Response<List<InboxUser>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            inboxUsers.clear();
                            inboxUsers.addAll(response.body());
                            Collections.sort(inboxUsers, (a, b) ->
                                    Long.compare(b.getLastMessageTimeMillis(), a.getLastMessageTimeMillis()));
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<InboxUser>> call, Throwable t) {
                        Log.e("InboxFragment", "Failed to load inbox users", t);
                    }
                });
    }

    private void openChat(InboxUser user) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, ChatFragment.newInstance(user.getUserId()));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
