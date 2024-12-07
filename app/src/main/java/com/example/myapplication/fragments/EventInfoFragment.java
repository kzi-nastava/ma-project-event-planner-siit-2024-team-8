package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.EventViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventInfoFragment newInstance(String param1, String param2) {
        EventInfoFragment fragment = new EventInfoFragment();
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

        View view =  inflater.inflate(R.layout.fragment_event_info, container, false);

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        EventViewPagerAdapter adapter = new EventViewPagerAdapter(requireActivity());
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(true);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView2);
        //NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.eventInfoFragmentLayout);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.overviewFragment) {
                viewPager.setCurrentItem(0, true);
            } else if (itemId == R.id.LocationFragment) {
                viewPager.setCurrentItem(1, true);
            } else if (itemId == R.id.budgetFragment) {
                viewPager.setCurrentItem(2, true);
            } else if (itemId == R.id.organizerInfoFragment) {
                viewPager.setCurrentItem(3, true);
            }
            return true;
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.overviewFragment);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.LocationFragment);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.budgetFragment);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.organizerInfoFragment);
                        break;
                }
            }
        });

        /*
        if (navHostFragment != null && bottomNavigationView!=null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
         */

        return view;
    }

}