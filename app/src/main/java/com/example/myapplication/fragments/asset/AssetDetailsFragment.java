package com.example.myapplication.fragments.asset;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.domain.Product;
import com.example.myapplication.domain.Utility;
import com.example.myapplication.services.ProductService;
import com.example.myapplication.services.UtilityService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetDetailsFragment extends Fragment {

    private String assetId;
    private String assetType;

    private LinearLayout utilityDetailsLayout;
    private TextView assetNameTextView, assetTypeTextView,
            assetDescriptionTextView, assetPriceTextView, assetDiscountTextView,
            assetActualPriceTextView, assetVisibilityTextView, assetAvailabilityTextView,
            assetConfirmationTextView, assetDurationTextView, assetBookingDeadlineTextView,
            assetCancellationDeadlineTextView;
    private MaterialButton editButton, deleteButton;

    private UtilityService utilityService;
    private ProductService productService;

    public AssetDetailsFragment() {
        // Required empty public constructor
    }

    public static AssetDetailsFragment newInstance(String assetId, String assetType) {
        AssetDetailsFragment fragment = new AssetDetailsFragment();
        Bundle args = new Bundle();
        args.putString("asset_id", assetId);
        args.putString("asset_type", assetType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assetId = getArguments().getString("asset_id");
            assetType = getArguments().getString("asset_type");
        }
        utilityService = new UtilityService();
        productService = new ProductService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_details, container, false);

        initializeViews(view);

        editButton.setOnClickListener(v -> navigateToEditFragment());
        deleteButton.setOnClickListener(v -> deleteAsset());

        String authHeader = "Bearer " + JwtTokenUtil.getToken();
        if ("UTILITY".equals(assetType)) {
            getUtilityById(authHeader, assetId);
        } else if ("PRODUCT".equals(assetType)) {
            getProductById(authHeader, assetId);
        }

        return view;
    }

    private void initializeViews(View view) {
        utilityDetailsLayout = view.findViewById(R.id.utilityDetailsLayout);
        assetNameTextView = view.findViewById(R.id.assetNameTextView);
        assetTypeTextView = view.findViewById(R.id.assetTypeTextView);
        assetDescriptionTextView = view.findViewById(R.id.assetDescriptionTextView);
        assetPriceTextView = view.findViewById(R.id.assetPriceTextView);
        assetDiscountTextView = view.findViewById(R.id.assetDiscountTextView);
        assetActualPriceTextView = view.findViewById(R.id.assetActualPriceTextView);
        assetVisibilityTextView = view.findViewById(R.id.assetVisibilityTextView);
        assetAvailabilityTextView = view.findViewById(R.id.assetAvailabilityTextView);
        assetConfirmationTextView = view.findViewById(R.id.assetConfirmationTextView);
        assetDurationTextView = view.findViewById(R.id.assetDurationTextView);
        assetBookingDeadlineTextView = view.findViewById(R.id.assetBookingDeadlineTextView);
        assetCancellationDeadlineTextView = view.findViewById(R.id.assetCancellationDeadlineTextView);
        editButton = view.findViewById(R.id.editButton);
        deleteButton = view.findViewById(R.id.deleteButton);
    }

    private void navigateToEditFragment() {
        EditAssetFragment fragment = EditAssetFragment.newInstance(assetId, assetType);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void deleteAsset() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.confirmation_dialog, null);
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.cancelButton).setOnClickListener(v -> dialog.dismiss());

        dialogView.findViewById(R.id.deleteButton).setOnClickListener(v -> {
            dialog.dismiss();
            callDeleteService();
        });

        dialog.show();
    }

    private void callDeleteService() {
        String authHeader = "Bearer " + JwtTokenUtil.getToken();
        if ("UTILITY".equals(assetType)) {
            utilityService.deleteUtility(authHeader, assetId, new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("AssetDetailsFragment", "Utility deleted successfully");
                        getParentFragmentManager().popBackStack();
                    } else {
                        Log.e("AssetDetailsFragment", "Failed to delete utility");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("AssetDetailsFragment", "Error: " + t.getMessage());
                }
            });
        } else if ("PRODUCT".equals(assetType)) {
            productService.deleteProduct(authHeader, assetId, new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("AssetDetailsFragment", "Product deleted successfully");
                        getParentFragmentManager().popBackStack();
                    } else {
                        Log.e("AssetDetailsFragment", "Failed to delete product");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("AssetDetailsFragment", "Error: " + t.getMessage());
                }
            });
        }
    }

    private void getUtilityById(String token, String id) {
        utilityService.getUtilityById(token, id, new Callback<Utility>() {
            @Override
            public void onResponse(Call<Utility> call, Response<Utility> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateUtilityData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Utility> call, Throwable t) {
                Log.e("AssetDetailsFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void getProductById(String token, String id) {
        productService.getProductById(token, id, new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateProductData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("AssetDetailsFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void populateUtilityData(Utility utility) {
        utilityDetailsLayout.setVisibility(View.VISIBLE);
        assetNameTextView.setText(utility.getName());
        assetTypeTextView.setText("Type: Utility");
        assetDescriptionTextView.setText("Description: " + utility.getDescription());
        double price = utility.getPrice();
        double discount = utility.getDiscount();
        double actualPrice = price - (price * discount / 100);
        assetPriceTextView.setText("Price: $" + price);
        assetDiscountTextView.setText("Discount: " + discount + "%");
        assetActualPriceTextView.setText("Actual Price: $" + String.format("%.2f", actualPrice));
        assetVisibilityTextView.setText("Visibility: " + utility.isVisible());
        assetAvailabilityTextView.setText("Available: " + utility.isAvailable());
        assetConfirmationTextView.setText("Manuel Confirmation: " + utility.isManuelConfirmation());
        assetDurationTextView.setText("Duration: " + utility.getDuration());
        assetBookingDeadlineTextView.setText("Booking Deadline: " + utility.getReservationTerm());
        assetCancellationDeadlineTextView.setText("Cancellation Deadline: " + utility.getCancellationTerm());
    }

    private void populateProductData(Product product) {
        utilityDetailsLayout.setVisibility(View.GONE);
        assetNameTextView.setText(product.getName());
        assetTypeTextView.setText("Type: Product");
        assetDescriptionTextView.setText("Description: " + product.getDescription());
        double price = product.getPrice();
        double discount = product.getDiscount();
        double actualPrice = price - (price * discount / 100);
        assetPriceTextView.setText("Price: $" + price);
        assetDiscountTextView.setText("Discount: " + discount + "%");
        assetActualPriceTextView.setText("Actual Price: $" + String.format("%.2f", actualPrice));
        assetVisibilityTextView.setText("Visibility: " + product.isVisible());
        assetAvailabilityTextView.setText("Available: " + product.isAvailable());
    }
}
