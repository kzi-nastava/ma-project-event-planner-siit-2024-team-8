package com.example.myapplication.fragments.asset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AssetCategoryAdapter;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.viewmodels.AssetCategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetCategoriesFragment extends Fragment implements AssetCategoryAdapter.OnItemClickListener {

    private RecyclerView activeRecyclerView;
    private RecyclerView pendingRecyclerView;
    private AssetCategoryAdapter activeAdapter;
    private AssetCategoryAdapter pendingAdapter;
    private AssetCategoryViewModel assetCategoryViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asset_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activeRecyclerView = view.findViewById(R.id.activeCategoriesRecyclerView);
        pendingRecyclerView = view.findViewById(R.id.pendingCategoriesRecyclerView);
        FloatingActionButton fab = view.findViewById(R.id.fabAddCategory);

        // Initialize ViewModel
        assetCategoryViewModel = new ViewModelProvider(this).get(AssetCategoryViewModel.class);

        activeRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        activeAdapter = new AssetCategoryAdapter(new ArrayList<>(), requireContext(), this);
        activeRecyclerView.setAdapter(activeAdapter);

        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        pendingAdapter = new AssetCategoryAdapter(new ArrayList<>(), requireContext(), this);
        pendingRecyclerView.setAdapter(pendingAdapter);

        fab.setOnClickListener(v -> showEditCategoryDialog(null, true)); // Add new category

        // Observing the live data for categories
        assetCategoryViewModel.getActiveCategories().observe(getViewLifecycleOwner(), new Observer<List<AssetCategory>>() {
            @Override
            public void onChanged(List<AssetCategory> activeCategories) {
                activeAdapter.updateCategories(activeCategories);
            }
        });

        assetCategoryViewModel.getPendingCategories().observe(getViewLifecycleOwner(), new Observer<List<AssetCategory>>() {
            @Override
            public void onChanged(List<AssetCategory> pendingCategories) {
                pendingAdapter.updateCategories(pendingCategories);
            }
        });

        assetCategoryViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
        });

        assetCategoryViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
            }
        });

        assetCategoryViewModel.loadCategories();
    }

    @Override
    public void onItemClick(AssetCategory category) {
        showEditCategoryDialog(category, false);
    }

    private void showEditCategoryDialog(AssetCategory category, boolean isCreateMode) {
        EditAssetCategoryPopup dialog = EditAssetCategoryPopup.newInstance(category, isCreateMode);
        dialog.setListener(new EditAssetCategoryPopup.EditAssetCategoryListener() {
            @Override
            public void onSaveCategory(AssetCategory updatedCategory) {
                assetCategoryViewModel.saveCategory(updatedCategory, isCreateMode, new Callback<AssetCategory>() {
                    @Override
                    public void onResponse(Call<AssetCategory> call, Response<AssetCategory> response) {
                        // Handle success
                        assetCategoryViewModel.loadCategories(); // Refresh categories
                    }

                    @Override
                    public void onFailure(Call<AssetCategory> call, Throwable t) {
                        // Handle failure
                    }
                });
            }

            @Override
            public void onDeleteCategory(AssetCategory categoryToDelete) {
                assetCategoryViewModel.deleteCategory(categoryToDelete, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // Handle success
                        assetCategoryViewModel.loadCategories(); // Refresh categories
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Handle failure
                    }
                });
            }

            @Override
            public void onCancel() {
                // Handle cancel
            }

            @Override
            public void onApproveCategory(AssetCategory category) {
                assetCategoryViewModel.approveCategory(category, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // Handle success
                        assetCategoryViewModel.loadCategories(); // Refresh categories
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Handle failure
                    }
                });
            }

            @Override
            public void onCategoryUpdated() {
                assetCategoryViewModel.loadCategories(); // Refresh categories after update
            }
        });
        dialog.show(getChildFragmentManager(), "EditCategoryDialog");
    }
}
