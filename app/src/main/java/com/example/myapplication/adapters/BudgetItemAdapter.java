package com.example.myapplication.adapters;

import static com.example.myapplication.adapters.BindingAdapters.bindAssetCategory;
import static com.example.myapplication.adapters.BindingAdapters.bindAssetCategoryByType;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.InverseBindingListener;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.BudgetItem;
import com.example.myapplication.domain.dto.BudgetItemCreateRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.security.auth.callback.Callback;

public class BudgetItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BudgetItem> budgetItems = new ArrayList<>();

    public List<BudgetItem> getBudgetItems(){
        return this.budgetItems;
    }

    OnNewBudgetItemClick onNewBudgetItemClick;

    public BudgetItemAdapter(List<BudgetItem> budgetItems,OnNewBudgetItemClick listener){
        this.budgetItems.addAll(budgetItems);
        this.onNewBudgetItemClick = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inputView = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item_input_card, parent, false);
        return new BudgetItemViewHolder(inputView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BudgetItemAdapter.BudgetItemViewHolder) holder).bind(budgetItems.get(position));
    }

    @Override
    public int getItemCount() {
        return budgetItems.size();
    }

    public void updateData(List<BudgetItem> newItems) {
        this.budgetItems.clear();
        this.budgetItems.addAll(newItems);
        notifyDataSetChanged();
    }

    public void addItem(BudgetItem budgetItem) {
        this.budgetItems.add(budgetItem);
        notifyItemInserted(budgetItems.size()-1);
    }

    public void removeItem (BudgetItem item){
        int position = budgetItems.indexOf(item);
        if (position != -1) {
            budgetItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public class BudgetItemViewHolder extends RecyclerView.ViewHolder{
        EditText plannedAmmount;
        RadioGroup assetCategoryRadioGroup;

        RadioButton utilityButton;
        RadioButton productButton;

        AppCompatSpinner assetCategorySpinner;

        BudgetItem item;

        Button removeButton;
        public BudgetItemViewHolder(@NonNull View itemView) {
            super(itemView);
            plannedAmmount = itemView.findViewById(R.id.plannedAmmountEditText);
            assetCategoryRadioGroup = itemView.findViewById(R.id.assetCategoryRadioGroup);
            utilityButton = itemView.findViewById(R.id.radioButtonUtility);
            productButton = itemView.findViewById(R.id.radioButtonProduct);
            assetCategorySpinner = itemView.findViewById(R.id.assetCateogriesSpinner);
            removeButton = itemView.findViewById(R.id.removeBudgetItemButton);

            removeButton.setOnClickListener(v -> onRemoveClick());
        }

        private void onRemoveClick() {
            removeItem(item);
        }

        public void bind(BudgetItem budgetItem) {
            this.item = budgetItem;
            if (budgetItem.getCategory() != null){
                if (Objects.equals(budgetItem.getCategory().getType(), "UTILITY")){
                    utilityButton.setChecked(true);
                }else{
                    productButton.setChecked(true);
                }
                bindAssetCategory(assetCategorySpinner, budgetItem.getCategory(), budgetItem.getCategory().getType(),null);
            }
            plannedAmmount.setText(budgetItem.getPlannedAmount().toString());

            assetCategoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

                String categoryType = (checkedId == R.id.radioButtonUtility) ? "UTILITY" : "PRODUCT";

                bindAssetCategory(assetCategorySpinner, budgetItem.getCategory()==null? null : budgetItem.getCategory(), categoryType, null);
            });

            assetCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    AssetCategory selectedCategory = (AssetCategory) view.getTag();

                    if (selectedCategory != null) {
                        budgetItem.setCategory(selectedCategory);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            plannedAmmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try{
                        budgetItem.setPlannedAmount( Double.parseDouble(plannedAmmount.getText().toString()));

                    }catch (NumberFormatException e){
                      return;
                    }
            }
            });
        }
    }

    public interface OnNewBudgetItemClick{
        void onNewBudgetItemClick();
    }
}
