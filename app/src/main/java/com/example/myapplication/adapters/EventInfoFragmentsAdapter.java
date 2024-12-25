package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.ToDoFragment;
import com.example.myapplication.fragments.event.event_info.EventOverviewFragment;

public class EventInfoFragmentsAdapter extends FragmentStateAdapter {
    public EventInfoFragmentsAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public EventInfoFragmentsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public EventInfoFragmentsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new EventOverviewFragment();
            default:
                return new ToDoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
