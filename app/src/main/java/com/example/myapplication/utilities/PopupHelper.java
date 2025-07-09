package com.example.myapplication.utilities;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.fragments.CreateEventTypeFragment;
import com.example.myapplication.fragments.asset.AssetCategoriesFragment;

public class PopupHelper {
    public static void showPopup(MainActivity activity, Fragment leftFragment, Fragment rightFragment,
                                 int leftButtonIcon, int rightButtonIcon,
                                 String leftText, String rightText ) {
        FrameLayout popupLayout = activity.findViewById(R.id.popupLayout);

        ImageButton leftButton = activity.findViewById(R.id.popupButtonLeft);
        ImageButton rightButton = activity.findViewById(R.id.popupButtonRight);
        leftButton.setImageResource(leftButtonIcon);
        rightButton.setImageResource(rightButtonIcon);

        TextView leftTextView = activity.findViewById(R.id.leftTextView);
        leftTextView.setText(leftText);
        TextView rightTextView = activity.findViewById(R.id.rightTextView);
        rightTextView.setText(rightText);


        popupLayout.setVisibility(View.VISIBLE);

        leftButton.setAlpha(0f);
        leftButton.setTranslationY(50f);
        leftButton.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(250)
                .start();

        rightButton.setAlpha(0f);
        rightButton.setTranslationX(-50f);
        rightButton.setTranslationY(50f);
        rightButton.animate()
                .alpha(1f)
                .translationX(0f)
                .translationY(0f)
                .setDuration(250)
                .setStartDelay(100)
                .start();

        popupLayout.setOnClickListener(v -> hide(leftButton, rightButton, popupLayout));

        leftButton.setOnClickListener(v -> {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, leftFragment)
                    .addToBackStack(null)
                    .commit();
            hide(leftButton, rightButton, popupLayout);
        });

        rightButton.setOnClickListener(v -> {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, rightFragment)
                    .addToBackStack(null)
                    .commit();
            hide(leftButton, rightButton, popupLayout);
        });
    }

    private static void hide(ImageButton btn1, ImageButton btn2, FrameLayout layout) {
        btn1.animate().alpha(0f).translationY(50f).setDuration(200).start();
        btn2.animate().alpha(0f).translationY(50f).setDuration(200).start();

        layout.postDelayed(() -> layout.setVisibility(View.GONE), 200);
    }
}
