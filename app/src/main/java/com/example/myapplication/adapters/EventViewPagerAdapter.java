package com.example.myapplication.adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.myapplication.fragments.EventOverviewFragment;
import com.example.myapplication.fragments.ToDoFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new EventOverviewFragment();
            case 1:
                return new ToDoFragment();
            case 2:
                return new ToDoFragment();
            case 3:
                return new ToDoFragment();
            default:
                throw new IllegalArgumentException("Invalid position");
        }
    }


    @Override
    public int getItemCount() {
        return 4; // Total number of fragments
    }
}