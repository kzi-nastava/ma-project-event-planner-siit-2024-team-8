package com.example.myapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Visibility;
import android.view.Menu;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.R;
import com.example.myapplication.domain.Role;
import com.example.myapplication.fragments.HomePageFragment;
import com.example.myapplication.fragments.LoginFragment;
import com.example.myapplication.fragments.event.create_event.CreateEventFragment;
import com.example.myapplication.fragments.event.event_info.EventInfoFragment;
import com.example.myapplication.viewmodels.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnRoleChangeListener {

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PERMISSION_REQUEST_CODE = 100;

    public ImageView imageView;
    public Uri imageUri = Uri.parse("");

    private BottomNavigationView bottomNavigationView;

    private NavHostFragment navHostFragment;

    private NavController navController;

    private FragmentTransaction loginTransaction;

    private LoginFragment loginFragment;

    private HomePageFragment homePageFragment;

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+ (API 33+)
            return ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED;
        } else {
            // For Android 12 and below
            return ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED;
        }
    }

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Request READ_MEDIA_IMAGES for Android 13+
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    PERMISSION_REQUEST_CODE
            );
        } else {
            // Request READ_EXTERNAL_STORAGE for Android 12 and below
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View rootView = findViewById(android.R.id.content);
        rootView.setOnApplyWindowInsetsListener((v, insets) -> {
            // Get the heights of status and navigation bars
            int statusBarHeight = insets.getInsets(WindowInsets.Type.statusBars()).top;
            int navBarHeight = insets.getInsets(WindowInsets.Type.navigationBars()).bottom;
            v.setPadding(0, statusBarHeight, 0, navBarHeight);
            return insets.consumeSystemWindowInsets();
        });
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewHomePage);
        navController = navHostFragment.getNavController();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new EventInfoFragment())
                .addToBackStack(null)
                .commit();

        /*loginFragment = new LoginFragment();
        FragmentTransaction loginTransaction = getSupportFragmentManager().beginTransaction();
        loginTransaction.replace(R.id.mainLayout,loginFragment)
                .commit();*/


    }

    @Override
    public void onRoleChanged(Role role) {
        getSupportFragmentManager().beginTransaction()
                        .detach(loginFragment)
                        .commit();
        bottomNavigationView.setVisibility(View.VISIBLE);
        Menu menu = bottomNavigationView.getMenu();
        if (role == Role.ORGANIZER) {
            bottomNavigationView.inflateMenu(R.menu.home_menu);
            NavGraph organizerGraph = navController.getNavInflater()
                    .inflate(R.navigation.home_user_navigation);
            navController.setGraph(organizerGraph);
        } else if (role == Role.USER) {
            bottomNavigationView.inflateMenu(R.menu.home_menu);
            bottomNavigationView.getMenu().findItem(R.id.createEventFragment).setVisible(false);
            NavGraph organizerGraph = navController.getNavInflater()
                    .inflate(R.navigation.home_user_navigation);
            navController.setGraph(organizerGraph);
        }
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}