package com.example.myapplication.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.databinding.FragmentProfileInfoBinding;
import com.example.myapplication.domain.enumerations.OfferingType;
import com.example.myapplication.domain.enumerations.Role;
import com.example.myapplication.domain.dto.user.UserInfoResponse;
import com.example.myapplication.fragments.asset.AssetCategoriesFragment;
import com.example.myapplication.fragments.asset.CreateAssetFragment;
import com.example.myapplication.fragments.asset.PriceListFragment;
import com.example.myapplication.fragments.event.create_event.CreateEventFragment;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.utilities.NetworkUtils;
import com.example.myapplication.viewmodels.UserViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;
import java.util.UUID;

public class ProfileInfoFragment extends Fragment {

    private UserInfoResponse userInfo;

    private UserViewModel userViewModel;

    private UUID userID;

    public ProfileInfoFragment() {
    }

    private FragmentProfileInfoBinding binding;

    public ProfileInfoFragment(UUID id){
        userID = id;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileInfoBinding.inflate(inflater,container,false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding.setUserVM(userViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
            if (userInfo != null) {
                displayUserProfile(userInfo,view);
            }
        });
        userViewModel.loadUserProfile(userID);
        setupProviderButtons();
        setupOrganizerButtons();
        setupAdminButtons();
        setupSelfInfoView(view);
        setupOtherUsersInfoView(view);

        ImageButton button = view.findViewById(R.id.edit_button);
        button.setOnClickListener(v -> onEditClicked());
    }


    private void setupOtherUsersInfoView(View view) {
        if (userID == null){
            return;
        }
        binding.reportButton.setVisibility(View.VISIBLE);
        binding.blockUserButton.setVisibility(View.VISIBLE);
        binding.logOutButton.setVisibility(View.GONE);
        binding.editButton.setVisibility(View.GONE);
        binding.blockedUsersButton.setVisibility(View.GONE);
        binding.favoriteEventsButton.setVisibility(View.GONE);

        binding.reportButton.setOnClickListener(v -> reportButtonClicked());
        binding.blockUserButton.setOnClickListener(v -> blockUserButtonClicked());
    }

    private void blockUserButtonClicked() {
        userViewModel.blockUser(userID,getContext());
        getParentFragmentManager().popBackStack();
    }


    private void reportButtonClicked() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.report_user_dialog_layout, null);
        dialog.setView(view);

        final AlertDialog alert = dialog.create();
        setUpDialog(alert); //setting up height and background
        Button cancel = (Button) view.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        Button ok = (Button) view.findViewById(R.id.ok_action);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText reasonET = view.findViewById(R.id.reasonEditText);
                String reason = reasonET.getText().toString();
                userViewModel.reportUser(UUID.fromString(JwtTokenUtil.getUserId()),reason,getContext());
            }
        });
        alert.show();
    }

    private void setupSelfInfoView(View view) {
        if(userID != null){
            return;
        }
        ImageButton backButton = view.findViewById(R.id.backButton);
        Button logOutButton = view.findViewById(R.id.logOutButton);
        Button blockedUsers = view.findViewById(R.id.blockedUsersButton);
        Button favoriteButton = view.findViewById(R.id.favoriteEventsButton);
        logOutButton.setOnClickListener(v -> logOut());
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        blockedUsers.setOnClickListener(v -> {
            BlockedUsersFragment fragment = new BlockedUsersFragment();
            fragment.show(getParentFragmentManager(),null);
        });
        favoriteButton.setOnClickListener(v -> {
            UserFavoriteEventsDialog favoriteEventsDialog = new UserFavoriteEventsDialog();
            favoriteEventsDialog.show(getParentFragmentManager(),null);
        });
        Role role = Objects.requireNonNull(JwtTokenUtil.getRole());
        switch(role) {
            case PROVIDER:
                binding.providerButtons.setVisibility(View.VISIBLE);
                userViewModel.loadProviderInfo(UUID.fromString(JwtTokenUtil.getUserId()));
                break;
            case ORGANIZER:
                binding.organizerButtons.setVisibility(View.VISIBLE);
                break;
            case ADMIN:
                binding.adminButtons.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void setupAdminButtons() {
        Button manageReviewsButton = binding.manageReviewsButton;
        MaterialButton viewReportsButton = binding.viewReportsButton;
        Button assetCategoriesButton = binding.assetCategoriesButtonAdmin;
        Button eventTypes = binding.eventTypesButtonAdmin;
        setupViewStatsButton();
        assetCategoriesButton.setOnClickListener(v -> replaceFragment(new AssetCategoriesFragment()));
        eventTypes.setOnClickListener(v -> replaceFragment(new CreateEventTypeFragment()));
        viewReportsButton.setOnClickListener(v -> replaceFragment(new ReportsFragment()));
        manageReviewsButton.setOnClickListener(v -> replaceFragment(new ReviewFragment()));

    }
    private void setupProviderButtons(){
        Button myAssetsButton = binding.myAssetsButton;
        Button createAssetButton = binding.createAssetButton;
        Button assetCategoriesButton = binding.assetCategoriesButton;
        Button priceListButton = binding.priceListButton;
        myAssetsButton.setOnClickListener(v -> replaceFragment(new AllSolutionsFragment(OfferingType.ASSET,JwtTokenUtil.getUserId())));
        createAssetButton.setOnClickListener(v -> replaceFragment(new CreateAssetFragment()));
        assetCategoriesButton.setOnClickListener(v -> replaceFragment(new AssetCategoriesFragment()));
        priceListButton.setOnClickListener(v -> replaceFragment(new PriceListFragment()));
    }

    private void setupOrganizerButtons(){
        Button myEventsButton = binding.myEventsButton;
        Button createEvent = binding.createEventButton;
        Button eventTypes = binding.eventTypesButton;
        setupViewStatsButton();
        myEventsButton.setOnClickListener(v -> replaceFragment(new AllSolutionsFragment(OfferingType.EVENT,JwtTokenUtil.getUserId())));
        createEvent.setOnClickListener(v-> replaceFragment(new CreateEventFragment()));
        eventTypes.setOnClickListener(v -> replaceFragment(new CreateEventTypeFragment()));
    }

    private void setupViewStatsButton(){
        if(JwtTokenUtil.getRole() == Role.PROVIDER  || JwtTokenUtil.getRole() == Role.USER){
            return;
        }
        Button viewStats = binding.viewEventStats;
        viewStats.setVisibility(View.VISIBLE);
        viewStats.setOnClickListener(v -> {
            ViewStatsDialogFragment fragment = new ViewStatsDialogFragment();
            fragment.show(getParentFragmentManager(),null);
        });
    }

    //Remove token as well as ID from Shared Preferences memory
    //Then removing backstack fragments and returning to startup fragment
    private void logOut(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.log_out_dialog_layout, null);
        dialog.setView(view);

        final AlertDialog alert = dialog.create();
        setUpDialog(alert);
        Button cancel = (Button) view.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        Button ok = (Button) view.findViewById(R.id.ok_action);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.hideNavView();
                JwtTokenUtil.logOut();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                replaceFragment(new StartupFragment());
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void setUpDialog(AlertDialog alert) {
        Window window = alert.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Transparent background

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());

            // Convert 300dp to pixels
            float density = getResources().getDisplayMetrics().density;
            int heightInPixels = (int) (300 * density);

            // Set dimensions
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = heightInPixels;

            window.setAttributes(layoutParams);
        }
    }

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }
    private void displayUserProfile(UserInfoResponse userInfo,View view) {
        ImageView profileImage = view.findViewById(R.id.profile_picture);
        Glide.with(requireContext())
                .load(NetworkUtils.replaceLocalhostWithDeviceIp(userInfo.profileImage))
                .placeholder(R.drawable.profile_placeholder) // Optional placeholder while loading// Optional error image
                .into(profileImage);
        TextView roleView = getView().findViewById(R.id.user_type);
        Role role = userID == null ? JwtTokenUtil.getRole() : userInfo.role;
        roleView.setText(String.valueOf(role));
        if (!String.valueOf(role).equalsIgnoreCase("PROVIDER")) {
            getView().findViewById(R.id.company_name_label).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.company_name).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.company_description_label).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.company_description).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.companyNameUnderLine).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.companyDescriptionUnderLine).setVisibility(View.INVISIBLE);
        }
    }

    public void onEditClicked() {
        ProfileEditFragment profileEditFragment = new ProfileEditFragment(userViewModel.getUserInfo().getValue(),userViewModel.getProviderInfo().getValue());
        replaceFragment(profileEditFragment);
    }
}
