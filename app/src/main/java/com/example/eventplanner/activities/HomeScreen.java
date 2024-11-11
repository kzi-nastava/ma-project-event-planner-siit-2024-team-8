package com.example.eventplanner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventCardAdapter;
import com.example.eventplanner.domain.AllAssetsType;
import com.example.eventplanner.domain.EventDTO;
import com.example.eventplanner.fragments.AssetsFragment;
import com.example.eventplanner.fragments.UserHomeFragment;

import java.sql.Date;
import java.util.ArrayList;

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
        if (view.getId() == R.id.seeAllEventsButton){
            AssetsFragment assetsFragment = new AssetsFragment(AllAssetsType.EVENT);
            fragmentManager.beginTransaction().
                    replace(R.id.fragment_layout,assetsFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}