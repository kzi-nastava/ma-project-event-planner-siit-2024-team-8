package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.fragments.CreateEventAgendaFragment;
import com.example.myapplication.fragments.CreateEventFragment;
import com.example.myapplication.fragments.CreateEventStepTwoFragment;
import com.example.myapplication.fragments.EventInfoFragment;
import com.example.myapplication.fragments.RegisterFragment;
import com.example.myapplication.fragments.StartupFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*CreateEventFragment fragment = new CreateEventFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main,fragment)
                .addToBackStack(null)
                .commit();*/
        RegisterFragment fragment = new RegisterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }
}