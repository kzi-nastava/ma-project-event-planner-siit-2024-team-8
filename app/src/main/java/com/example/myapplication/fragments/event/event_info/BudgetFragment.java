package com.example.myapplication.fragments.event.event_info;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.BudgetItemEditAdapter;
import com.example.myapplication.databinding.AddBudgetItemDialogBinding;
import com.example.myapplication.domain.Budget;
import com.example.myapplication.domain.BudgetItem;
import com.example.myapplication.services.BudgetService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.example.myapplication.fragments.event.event_info.AddBudgetItemDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetFragment extends Fragment {
    private String eventId = "";
    private final BudgetService budgetService = new BudgetService();
    private String budgetId = "";
    private RecyclerView budgetRecyclerView;
    private BudgetItemEditAdapter budgetItemEditAdapter;
    private List<BudgetItem> budgetItems = new ArrayList<>();
    private TextView totalPlannedBudget, totalSpentBudget;

    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.budget, container, false);

        budgetRecyclerView = view.findViewById(R.id.budget_items_recycler_view);
        totalPlannedBudget = view.findViewById(R.id.total_planned_budget);
        totalSpentBudget = view.findViewById(R.id.total_spent_budget);
        MaterialButton addBudgetItemButton = view.findViewById(R.id.add_budget_item_button);
        addBudgetItemButton.setOnClickListener(v -> {
            openAddBudgetItemDialog();
        });

        budgetItemEditAdapter = new BudgetItemEditAdapter(budgetItems, new BudgetItemEditAdapter.OnBudgetItemClickListener() {
            @Override
            public void onItemClicked(BudgetItem item) {
                Toast.makeText(getContext(), "Item clicked: " + item.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemUpdated(BudgetItem item) {
                budgetService.updateBudgetItem(item.getId(), item.getPlannedAmount(), new Callback<BudgetItem>() {
                    @Override
                    public void onResponse(Call<BudgetItem> call, Response<BudgetItem> response) {
                        if (response.isSuccessful()) {
                            showToast("Budget item updated");
                            updateBudgetTotals();
                        } else {
                            showToast("Failed to update item");
                        }
                    }

                    @Override
                    public void onFailure(Call<BudgetItem> call, Throwable t) {
                        showToast("Error: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onItemDeleted(BudgetItem item) {
                budgetService.deleteBudgetItem(item.getId(), new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            budgetItems.remove(item);
                            budgetItemEditAdapter.notifyDataSetChanged();
                            updateBudgetTotals();
                            showToast("Item deleted");
                        } else {
                            showToast("Failed to delete item");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        showToast("Error: " + t.getMessage());
                    }
                });
            }
        });

        budgetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        budgetRecyclerView.setAdapter(budgetItemEditAdapter);

        loadBudgetData();

        return view;
    }

    public void loadBudgetData() {
        budgetService.getBudgetByEventId(eventId, new Callback<Budget>() {
            @Override
            public void onResponse(Call<Budget> call, Response<Budget> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Budget budget = response.body();
                    budgetItems.clear();
                    budgetItems.addAll(budget.getBudgetItems());
                    budgetId = String.valueOf(budget.getId());
                    budgetItemEditAdapter.notifyDataSetChanged();
                    budgetRecyclerView.setVisibility(View.VISIBLE);
                    updateBudgetTotals();
                } else {
                    showToast("Failed to load budget data");
                }
            }

            @Override
            public void onFailure(Call<Budget> call, Throwable t) {
                showToast("Error: " + t.getMessage());
            }
        });
    }

    private void updateBudgetTotals() {
        double totalPlanned = 0.0;
        double totalSpent = 0.0;

        for (BudgetItem item : budgetItems) {
            totalPlanned += item.getPlannedAmount();
            totalSpent += item.getActualAmount();
        }

        totalPlannedBudget.setText("Total Planned Budget: $" + String.format("%.2f", totalPlanned) + '\n');
        totalSpentBudget.setText("Total Spent: $" + String.format("%.2f", totalSpent));
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void openAddBudgetItemDialog() {
        AddBudgetItemDialogFragment addBudgetItemDialogFragment = new AddBudgetItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("eventId", budgetId);
        addBudgetItemDialogFragment.setArguments(args);
        addBudgetItemDialogFragment.setTargetFragment(this, 0); // Set the target fragment (BudgetFragment) to receive results
        addBudgetItemDialogFragment.show(getParentFragmentManager(), "AddBudgetItemDialogFragment");
    }
}