package com.example.myapplication.fragments.asset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AssetCategoryAdapter;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.utilities.JwtTokenUtil;
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
    private List<AssetCategory> activeCategories;
    private List<AssetCategory> pendingCategories;

    private AssetCategoryService assetCategoryService;

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

        activeCategories = new ArrayList<>();
        pendingCategories = new ArrayList<>();

        // Temporary static categories
        addStaticCategories();

        activeRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        activeAdapter = new AssetCategoryAdapter(activeCategories, requireContext(), this);
        activeRecyclerView.setAdapter(activeAdapter);

        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        pendingAdapter = new AssetCategoryAdapter(pendingCategories, requireContext(), this);
        pendingRecyclerView.setAdapter(pendingAdapter);

        fab.setOnClickListener(v -> {
            EditAssetCategoryPopup dialog = EditAssetCategoryPopup.newInstance(null, true);
            dialog.setListener(new EditAssetCategoryPopup.EditAssetCategoryListener() {
                @Override
                public void onSaveCategory(AssetCategory updatedCategory) {
                    pendingCategories.add(updatedCategory);
                    pendingAdapter.notifyItemInserted(pendingCategories.size() - 1);
                }

                @Override
                public void onDeleteCategory(AssetCategory categoryToDelete) {
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onApproveCategory(AssetCategory category) {
                    category.setActive(true);
                    pendingCategories.remove(category);
                    activeCategories.add(category);
                    activeAdapter.notifyItemInserted(activeCategories.size() - 1);
                }
            });
            dialog.show(getChildFragmentManager(), "AddCategoryDialog");
        });

        // Uncomment this line when dynamic loading is required:
        // loadCategories();
    }

    private void addStaticCategories() {
        activeCategories.add(new AssetCategory("0", "Active Category 1", "Description for active category 1", "Utility", true));
        activeCategories.add(new AssetCategory("0", "Active Category 2", "Description for active category 2", "Product", true));

        pendingCategories.add(new AssetCategory("0", "Pending Category 1", "Description for pending category 1", "Utility", false));
        pendingCategories.add(new AssetCategory("0", "Pending Category 2", "Description for pending category 2", "Product", false));

//        activeAdapter.notifyDataSetChanged();
//        pendingAdapter.notifyDataSetChanged();
    }

    private void loadCategories() {
        String token = JwtTokenUtil.getToken();

        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            assetCategoryService.getActiveCategories(authHeader, new Callback<List<AssetCategory>>() {
                @Override
                public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        activeCategories.clear();
                        activeCategories.addAll(response.body());
                        //activeAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                }
            });

            assetCategoryService.getPendingCategories(authHeader, new Callback<List<AssetCategory>>() {
                @Override
                public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        pendingCategories.clear();
                        pendingCategories.addAll(response.body());
//                        pendingAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                }


            });
        }
    }

    @Override
    public void onItemClick(AssetCategory category) {
        EditAssetCategoryPopup dialog = EditAssetCategoryPopup.newInstance(category, false);
        dialog.setListener(new EditAssetCategoryPopup.EditAssetCategoryListener() {
            @Override
            public void onSaveCategory(AssetCategory updatedCategory) {
                int index = getCategoryIndex(updatedCategory);
                if (index != -1) {
                    if (activeCategories.contains(updatedCategory)) {
                        activeCategories.set(index, updatedCategory);
                        activeAdapter.notifyItemChanged(index);
                    } else {
                        pendingCategories.set(index, updatedCategory);
                        pendingAdapter.notifyItemChanged(index);
                    }
                }
            }

            @Override
            public void onDeleteCategory(AssetCategory categoryToDelete) {
                if (activeCategories.contains(categoryToDelete)) {
                    activeCategories.remove(categoryToDelete);
//                    activeAdapter.notifyDataSetChanged();
                } else if (pendingCategories.contains(categoryToDelete)) {
                    pendingCategories.remove(categoryToDelete);
//                    pendingAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onApproveCategory(AssetCategory category) {
                category.setActive(true);
                pendingCategories.remove(category);
                activeCategories.add(category);
                activeAdapter.notifyItemInserted(activeCategories.size() - 1);
            }
        });
        dialog.show(getChildFragmentManager(), "EditCategoryDialog");
    }

    private int getCategoryIndex(AssetCategory category) {
        int index = -1;
        if (activeCategories.contains(category)) {
            index = activeCategories.indexOf(category);
        } else if (pendingCategories.contains(category)) {
            index = pendingCategories.indexOf(category);
        }
        return index;
    }
}
