package com.example.myapplication.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.domain.Role;
import com.example.myapplication.fragments.ToDoFragment;
import com.example.myapplication.fragments.event.event_info.EventOverviewFragment;
import com.example.myapplication.utilities.JwtTokenUtil;

public class EventInfoFragmentsAdapter extends FragmentStateAdapter {
    private String eventId;

    public EventInfoFragmentsAdapter(@NonNull Fragment fragment, String eventId) {
        super(fragment);
        this.eventId = eventId;
    }

    public EventInfoFragmentsAdapter(@NonNull FragmentActivity fragmentActivity, String eventId) {
        super(fragmentActivity);
        this.eventId = eventId;
    }

    public EventInfoFragmentsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String eventId) {
        super(fragmentManager, lifecycle);
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                // Pass the eventId to EventOverviewFragment
                EventOverviewFragment eventOverviewFragment = new EventOverviewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("eventId", eventId);
                eventOverviewFragment.setArguments(bundle);
                return eventOverviewFragment;
            default:
                return new ToDoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return JwtTokenUtil.getRole() == Role.ORGANIZER ? 4 : 3;
    }
}
