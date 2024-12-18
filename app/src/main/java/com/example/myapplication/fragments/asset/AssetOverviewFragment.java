package com.example.myapplication.fragments.asset;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.Product;
import com.example.myapplication.domain.Utility;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.services.ProductService;
import com.example.myapplication.services.UtilityService;
import com.example.myapplication.utilities.JwtTokenUtil;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetOverviewFragment extends Fragment {

    private String assetId;
    private String assetType;

    private TextView assetNameTextView;
    private TextView assetTypeTextView;
    private TextView assetCategoryTextView;
    private TextView assetDescriptionTextView;
    private TextView assetPriceTextView;
    private TextView assetDiscountTextView;
    private TextView assetActualPriceTextView;
    private LinearLayout utilityDetailsLayout;
    private TextView assetDurationTextView;

    private TextView assetBookingDeadlineTextView;

    private TextView assetCancellationDeadlineTextView;

    private UtilityService utilityService;
    private ProductService productService;


    public AssetOverviewFragment() {
        // Required empty public constructor
    }

    public static AssetOverviewFragment newInstance(String assetId, String assetType) {
        AssetOverviewFragment fragment = new AssetOverviewFragment();
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

        // Initialize services
        utilityService = new UtilityService();
        productService = new ProductService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_overview, container, false);

        // Initialize the views
        assetNameTextView = view.findViewById(R.id.assetNameTextView);
        assetTypeTextView = view.findViewById(R.id.assetTypeTextView);
        assetCategoryTextView = view.findViewById(R.id.assetCategoryTextView);
        assetDescriptionTextView = view.findViewById(R.id.assetDescriptionTextView);
        assetPriceTextView = view.findViewById(R.id.assetPriceTextView);
        assetDiscountTextView = view.findViewById(R.id.assetDiscountTextView);
        assetActualPriceTextView = view.findViewById(R.id.assetActualPriceTextView);
        utilityDetailsLayout = view.findViewById(R.id.utilityDetailsLayout);

        String authHeader = "Bearer " + JwtTokenUtil.getToken();
        Log.e("debug", assetId + " i ovo je type " + assetType);
        if ("UTILITY".equals(assetType)) {
            getUtilityById(authHeader, assetId);
        } else if ("PRODUCT".equals(assetType)) {
            getProductById(authHeader, assetId);
        }
        return view;
    }

    private void getUtilityById(String token, String id) {
        utilityService.getUtilityById(token, id, new Callback<Utility>() {
            @Override
            public void onResponse(Call<Utility> call, Response<Utility> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Utility utility = response.body();
                    Log.d("AssetOverviewFragment", "Received utility: " + utility.getName());
                    populateUtilityData(utility);
                } else {
                    // Log detailed error information
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.d("AssetOverviewFragment", "Request URL: " + call.request().url());
                    } catch (IOException e) {
                        Log.e("AssetOverviewFragment", "Error reading error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Utility> call, Throwable t) {
                // Log the full exception details
                Log.e("AssetOverviewFragment", "Request failed: " + t.getMessage(), t);
            }
        });
    }

    private void getProductById(String token, String id) {
        productService.getProductById(token, id, new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    Log.d("AssetOverviewFragment", "Received product: " + product.getName());
                    populateProductData(product);
                } else {
                    Log.e("error", response.errorBody() + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void populateUtilityData(Utility utility) {
        assetNameTextView.setText(utility.getName());
        assetTypeTextView.setText("Utility");

        // Fetch the category name using AssetCategoryService
        String categoryId = utility.getCategory();
        String authHeader = "Bearer " + JwtTokenUtil.getToken();
        AssetCategoryService categoryService = new AssetCategoryService();
        categoryService.getCategoryById(authHeader, categoryId, new Callback<AssetCategory>() {
            @Override
            public void onResponse(Call<AssetCategory> call, Response<AssetCategory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AssetCategory category = response.body();
                    assetCategoryTextView.setText("Category " + category.getName());  // Set category name
                } else {
                    Log.e("AssetOverviewFragment", "Failed to fetch category for utility");
                }
            }

            @Override
            public void onFailure(Call<AssetCategory> call, Throwable t) {
                Log.e("AssetOverviewFragment", "Category request failed: " + t.getMessage(), t);
            }
        });

        assetDescriptionTextView.setText(utility.getDescription());
        double price = utility.getPrice();
        double discount = utility.getDiscount();
        double actualPrice = price - (price * discount / 100);
        assetPriceTextView.setText("Price: $" + price);
        assetDiscountTextView.setText("Discount: " + discount + "%");
        assetActualPriceTextView.setText("Actual Price: $" + String.format("%.2f", actualPrice));
        utilityDetailsLayout.setVisibility(View.VISIBLE);
        assetDurationTextView.setText("Duration: " + utility.getDuration() + " hours");
        assetBookingDeadlineTextView.setText("Booking Deadline: " + utility.getReservationTerm());
        assetCancellationDeadlineTextView.setText("Cancellation Deadline: " + utility.getCancellationTerm());
    }

    private void populateProductData(Product product) {
        assetNameTextView.setText(product.getName());
        assetTypeTextView.setText("Product");

        String categoryId = product.getCategory();
        String authHeader = "Bearer " + JwtTokenUtil.getToken();
        AssetCategoryService categoryService = new AssetCategoryService();
        categoryService.getCategoryById(authHeader, categoryId, new Callback<AssetCategory>() {
            @Override
            public void onResponse(Call<AssetCategory> call, Response<AssetCategory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AssetCategory category = response.body();
                    assetCategoryTextView.setText("Category " + category.getName());
                } else {
                    Log.e("AssetOverviewFragment", "Failed to fetch category for product");
                }
            }

            @Override
            public void onFailure(Call<AssetCategory> call, Throwable t) {
                Log.e("AssetOverviewFragment", "Category request failed: " + t.getMessage(), t);
            }
        });

        assetDescriptionTextView.setText(product.getDescription());
        double price = product.getPrice();
        double discount = product.getDiscount();
        double actualPrice = price - (price * discount / 100);
        assetPriceTextView.setText("Price: $" + price);
        assetDiscountTextView.setText("Discount: " + discount + "%");
        assetActualPriceTextView.setText("Actual Price: $" + String.format("%.2f", actualPrice));
        utilityDetailsLayout.setVisibility(View.GONE);
    }
}