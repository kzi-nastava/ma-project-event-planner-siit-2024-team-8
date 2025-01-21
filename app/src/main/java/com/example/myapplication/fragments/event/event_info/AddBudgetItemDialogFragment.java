package com.example.myapplication.fragments.event.event_info;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.domain.BudgetItem;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.dto.BudgetItemCreateRequest;
import com.example.myapplication.fragments.event.event_info.BudgetFragment;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.services.BudgetService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBudgetItemDialogFragment extends DialogFragment {

    private RadioGroup assetCategoryRadioGroup;
    private Spinner assetCategoriesSpinner;
    private BudgetService budgetService;
    private TextInputEditText plannedAmountEditText;

    private String eventId;

    private final AssetCategoryService assetCategoryService = new AssetCategoryService();

    public AddBudgetItemDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_budget_item_dialog, container, false);
        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
        }
        assetCategoryRadioGroup = view.findViewById(R.id.assetCategoryRadioGroup);
        assetCategoriesSpinner = view.findViewById(R.id.assetCateogriesSpinner);
        plannedAmountEditText = view.findViewById(R.id.plannedAmmountEditText);
        budgetService = new BudgetService();

        assetCategoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String categoryType = null;
            if (checkedId == R.id.radioButtonProduct) {
                categoryType = "Product";
            } else if (checkedId == R.id.radioButtonUtility) {
                categoryType = "Utility";
            }

            loadAssetCategories(categoryType);
        });

        Button addButton = view.findViewById(R.id.addBudgetItemButton);
        addButton.setOnClickListener(v -> {
            String plannedAmountStr = plannedAmountEditText.getText().toString();

            if (plannedAmountStr.isEmpty()) {
                Toast.makeText(getContext(), "Planned amount is required", Toast.LENGTH_SHORT).show();
                return;
            }

            double plannedAmount;
            try {
                plannedAmount = Double.parseDouble(plannedAmountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Please enter a valid number for the planned amount", Toast.LENGTH_SHORT).show();
                return;
            }

            AssetCategory selectedAssetCategory = (AssetCategory) assetCategoriesSpinner.getSelectedItem();
            if (selectedAssetCategory == null) {
                Toast.makeText(getContext(), "Please select a valid asset category", Toast.LENGTH_SHORT).show();
                return;
            }

            BudgetItemCreateRequest budgetItemCreateRequest = new BudgetItemCreateRequest();
            budgetItemCreateRequest.setPlannedAmount(plannedAmount);
            budgetItemCreateRequest.setAssetCategoryId(UUID.fromString(selectedAssetCategory.getId()));  // Using the selected AssetCategory's ID

            addBudgetItem(budgetItemCreateRequest);
        });

        return view;
    }

    private void loadAssetCategories(String categoryType) {
        // Get categories based on the selected category type
        if ("Product".equals(categoryType)) {
            assetCategoryService.getActiveProductCategories("token", new Callback<List<AssetCategory>>() {
                @Override
                public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateCategorySpinner(response.body());
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch product categories", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if ("Utility".equals(categoryType)) {
            assetCategoryService.getActiveUtilityCategories("token", new Callback<List<AssetCategory>>() {
                @Override
                public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateCategorySpinner(response.body());
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch utility categories", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateCategorySpinner(List<AssetCategory> categories) {
        ArrayAdapter<AssetCategory> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        assetCategoriesSpinner.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void addBudgetItem(BudgetItemCreateRequest budgetItemCreateRequest) {
        budgetService.addBudgetItem(this.eventId, budgetItemCreateRequest, new Callback<BudgetItem>() {
            @Override
            public void onResponse(Call<BudgetItem> call, Response<BudgetItem> response) {
                if (response.isSuccessful()) {
                    dismiss();

                    if (getTargetFragment() instanceof BudgetFragment) {
                        ((BudgetFragment) getTargetFragment()).loadBudgetData();
                    }

                    Toast.makeText(getContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Log the error response for better debugging
                    try {
                        String errorResponse = response.errorBody() != null ? response.errorBody().string() : "No error body available";
                        Log.e("AddBudgetItem", "Error Response: " + errorResponse);
                    } catch (Exception e) {
                        Log.e("AddBudgetItem", "Error reading error body: " + e.getMessage());
                    }

                    // Show a generic failure message
                    Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BudgetItem> call, Throwable t) {
                // Log the failure details (network error, etc.)
                Log.e("AddBudgetItem", "Network Failure: " + t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}