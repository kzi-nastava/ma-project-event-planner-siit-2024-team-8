package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.NotificationAdapter;
import com.example.myapplication.domain.Notification;
import com.example.myapplication.domain.dto.NotificationResponse;
import com.example.myapplication.services.BudgetService;
import com.example.myapplication.services.ClientUtils;
import com.example.myapplication.services.NotificationService;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.viewmodels.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  NotificationsFragment extends Fragment implements NotificationService.NotificationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NotificationAdapter adapter;

    private RecyclerView notificationsRecyclerView;

    private UserViewModel userVM;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userVM = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        setupNotificationRecyclerView(view);
        userVM.getNotifications().observe(getViewLifecycleOwner(), notificationResponses -> {
            adapter.setData(notificationResponses);
        });
        userVM.getUserNotifications();
        view.findViewById(R.id.markAllAsRead).setOnClickListener(v -> {
            markAllAsRead();
            adapter.markAllRead();
        });

    }

    private void markAllAsRead() {
        userVM.markAllAsRead();
    }

    private void setupNotificationRecyclerView(View view) {
        notificationsRecyclerView = view.findViewById(R.id.notificationsRecyclerView);
        adapter = new NotificationAdapter(this);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onNotificationReceived(Notification notification) {
        //EMPTY
    }

    @Override
    public void onNotificationClicked(NotificationResponse notification) {
        if (notification.isClickable() && notification.getReservationId() != null) {
            reserveNotification(notification);
        }
        // Mark as seen if not already
        if (!notification.isSeen()) {
            ClientUtils.notifcationsAPIService.markAsSeen(notification.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }

    private void reserveNotification(NotificationResponse notificationResponse) {
        BudgetService budgetService = new BudgetService();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reservation")
                .setMessage("Do you want to accept or deny the reservation?")
                .setPositiveButton("Accept", (dialog, which) -> {
                    // Accept reservation
                    budgetService.acceptReservation(notificationResponse.getReservationId());
                })
                .setNegativeButton("Deny", (dialog, which) -> {
                    // Deny reservation
                    budgetService.denyReservation(notificationResponse.getReservationId());
                })
                .setNeutralButton("Cancel", (dialog, which) -> {
                    Log.d("Reservation", "Dialog closed without action");
                    dialog.dismiss();
                })
                .show();
    }
}