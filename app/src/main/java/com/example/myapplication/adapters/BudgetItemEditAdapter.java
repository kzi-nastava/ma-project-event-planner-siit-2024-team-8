package com.example.myapplication.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.BudgetItem;
import com.example.myapplication.domain.dto.event.BudgetItemResponse;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class BudgetItemEditAdapter extends RecyclerView.Adapter<BudgetItemEditAdapter.BudgetItemEditViewHolder> {
    private final List<BudgetItemResponse> budgetItems;
    private final OnBudgetItemClickListener listener;
    private static final AssetCategoryService assetCategoryService = new AssetCategoryService();

    public interface OnBudgetItemClickListener {
        void onItemClicked(BudgetItemResponse item);
        void onItemUpdated(BudgetItemResponse item);
        void onItemDeleted(BudgetItemResponse item);

        void onShowBoughtAssets(BudgetItemResponse item,AssetCategory category);
    }

    public BudgetItemEditAdapter(List<BudgetItemResponse> budgetItems, OnBudgetItemClickListener listener) {
        this.budgetItems = budgetItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BudgetItemEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.budget_item_edit_card, parent, false);
        return new BudgetItemEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetItemEditViewHolder holder, int position) {
        BudgetItemResponse item = budgetItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return budgetItems.size();
    }

    static class BudgetItemEditViewHolder extends RecyclerView.ViewHolder {
        private final EditText plannedAmountEditText;
        private final TextView itemName;
        private final TextView spentBudgetText;
        private final ImageButton deleteButton;
        private final TextView categoryTypeText;

        private final MaterialButton showBoughtAssets;

        private AssetCategory category = null;



        public BudgetItemEditViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.titleText);
            categoryTypeText = itemView.findViewById(R.id.categoryTypeText);
            plannedAmountEditText = itemView.findViewById(R.id.plannedBudgetEditText);
            spentBudgetText = itemView.findViewById(R.id.spentBudgetText);
            deleteButton = itemView.findViewById(R.id.removeButton);
            showBoughtAssets = itemView.findViewById(R.id.showBoughtAssetsButton);
        }

        public void bind(BudgetItemResponse item, OnBudgetItemClickListener listener) {
            assetCategoryService.getCategoryById(JwtTokenUtil.getToken(), item.getAssetCategoryId().toString(), new Callback<AssetCategory>() {
                @Override
                public void onResponse(Call<AssetCategory> call, Response<AssetCategory> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        category = response.body();
                        itemName.setText(category.getName());
                        categoryTypeText.setText(category.getType());

                    } else {
                        Log.e("AssetCategory", "Failed to fetch category for budget item");
                    }
                }

                @Override
                public void onFailure(Call<AssetCategory> call, Throwable t) {
                    Log.e("AssetCategory", "Category request failed: " + t.getMessage(), t);
                }
            });

            plannedAmountEditText.setHint(String.valueOf(item.getPlannedAmount()));
            spentBudgetText.setText("Spent budget: " + String.valueOf(item.getActualAmount()));

            plannedAmountEditText.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    try {
                        double newAmount = Double.parseDouble(plannedAmountEditText.getText().toString());
                        item.setPlannedAmount(newAmount);
                        listener.onItemUpdated(item);
                    } catch (NumberFormatException e) {
                        plannedAmountEditText.setError("Invalid amount");
                    }
                }
            });

            itemView.setOnClickListener(v -> listener.onItemClicked(item));
            deleteButton.setOnClickListener(v -> listener.onItemDeleted(item));
            showBoughtAssets.setOnClickListener(v -> listener.onShowBoughtAssets(item,category));
        }
    }
}