package com.example.eventplanner.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.domain.OfferingType;
import com.example.eventplanner.fragments.OfferingsFragment;
import com.example.eventplanner.fragments.UserHomeFragment;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView eventRecyclerView = findViewById(R.id.eventRecyclerView);
        UserHomeFragment fragment = new UserHomeFragment(eventRecyclerView);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_layout,fragment).commit();

    }

    public void onClickButton(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        OfferingsFragment fragment = new OfferingsFragment();
        if (view.getId() == R.id.seeAllEventsButton){
            fragment.setType(OfferingType.EVENT);
        }else if (view.getId() == R.id.seeAllAssetsButton){
            fragment.setType(OfferingType.ASSET);
        }
        fragmentManager.beginTransaction().
                replace(R.id.fragment_layout,fragment)
                .addToBackStack(null)
                .commit();
    }
}