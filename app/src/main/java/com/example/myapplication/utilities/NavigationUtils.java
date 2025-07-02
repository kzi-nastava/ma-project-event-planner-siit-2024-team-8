package com.example.myapplication.utilities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;

public class NavigationUtils {
    public static void safeNavigate(@NonNull NavController navController, int destinationId) {
        NavDestination currentDestination = navController.getCurrentDestination();
        if (currentDestination == null || currentDestination.getId() == destinationId) {
            // Already on destination, do nothing
            return;
        }

        NavOptions navOptions = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                // Pop up to the start destination of the graph (clears backstack)
                .setPopUpTo(navController.getGraph().getStartDestinationId(), false)
                .setRestoreState(true)
                .build();

        try {
            navController.navigate(destinationId, null, navOptions);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Catch navigation errors safely
            e.printStackTrace();
        }
    }
}