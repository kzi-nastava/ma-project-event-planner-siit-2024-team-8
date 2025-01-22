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
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.utilities.JwtTokenUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class BudgetItemEditAdapter extends RecyclerView.Adapter<BudgetItemEditAdapter.BudgetItemEditViewHolder> {
    private final List<BudgetItem> budgetItems;
    private final OnBudgetItemClickListener listener;
    private static final AssetCategoryService assetCategoryService = new AssetCategoryService();

    public interface OnBudgetItemClickListener {
        void onItemClicked(BudgetItem item);
        void onItemUpdated(BudgetItem item);
        void onItemDeleted(BudgetItem item);
    }

    public BudgetItemEditAdapter(List<BudgetItem> budgetItems, OnBudgetItemClickListener listener) {
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
        BudgetItem item = budgetItems.get(position);
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

        public BudgetItemEditViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.titleText);
            categoryTypeText = itemView.findViewById(R.id.categoryTypeText);
            plannedAmountEditText = itemView.findViewById(R.id.plannedBudgetEditText);
            spentBudgetText = itemView.findViewById(R.id.spentBudgetText);
            deleteButton = itemView.findViewById(R.id.removeButton);
        }

        public void bind(BudgetItem item, OnBudgetItemClickListener listener) {
            assetCategoryService.getCategoryById(JwtTokenUtil.getToken(), item.getCategory(), new Callback<AssetCategory>() {
                @Override
                public void onResponse(Call<AssetCategory> call, Response<AssetCategory> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AssetCategory category = response.body();
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
        }
    }
}