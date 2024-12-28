package com.example.myapplication.fragments.event.event_info;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.EventInfoFragmentsAdapter;
import com.example.myapplication.domain.Role;
import com.example.myapplication.fragments.ToDoFragment;
import com.example.myapplication.fragments.event.edit_event.EventEditFragment;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class EventInfoFragment extends Fragment {
    public String eventId = "";

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    public static EventInfoFragment newInstance(String eventId) {
        EventInfoFragment fragment = new EventInfoFragment();
        Bundle args = new Bundle();
        args.putString("eventId", eventId); // Put the eventId in the arguments bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);

        tabLayout = view.findViewById(R.id.tabLayoutEventInfo);
        viewPager = view.findViewById(R.id.viewPagerEventInfo);

        // Pass eventId to the adapter
        EventInfoFragmentsAdapter adapter = new EventInfoFragmentsAdapter(this, eventId);
        viewPager.setAdapter(adapter);

        adjustTabLayoutByRole(view);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (JwtTokenUtil.getRole() == Role.ORGANIZER){
                switch (position) {
                    case 0:
                        tab.setText("Overview");
                        break;
                    case 1:
                        tab.setText("Guests");

                        break;
                    case 2:
                        tab.setText("Agenda");
                        break;
                    case 3:
                        tab.setText("Budget");
                        break;
                    default:
                        throw new IllegalStateException("Unexpected position " + position);
                }
            }else{
                switch (position) {
                    case 0:
                        tab.setText("Overview");
                        break;
                    case 1:
                        tab.setText("Guests");
                        break;
                    case 2:
                        tab.setText("Agenda");
                        break;
                    default:
                        throw new IllegalStateException("Unexpected position " + position);
                }
            }
        }).attach();



        return view;
    }

    private void adjustTabLayoutByRole(View view) {
        Role role = JwtTokenUtil.getRole();
        if (role != Role.ORGANIZER) {
            TabLayout tabLayout = view.findViewById(R.id.tabLayoutEventInfo);
            TabLayout.Tab tab = tabLayout.getTabAt(3);
            assert tab != null;
            tab.view.setVisibility(View.GONE);
        }
    }
}
