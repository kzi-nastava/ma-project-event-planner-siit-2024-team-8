package com.example.myapplication.fragments;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AssetCategoriesFragment extends Fragment implements AssetCategoryAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private AssetCategoryAdapter adapter;
    private List<AssetCategory> categories;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asset_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.assetCategoriesRecyclerView);
        FloatingActionButton fab = view.findViewById(R.id.fabAddCategory);

        categories = new ArrayList<>();
        categories.add(new AssetCategory("Category 1", "Description for Category 1"));
        categories.add(new AssetCategory("Category 2", "Description for Category 2"));
        categories.add(new AssetCategory("Category 3", "Description for Category 3"));
        categories.add(new AssetCategory("Category 4", "Description for Category 4"));
        categories.add(new AssetCategory("Category 5", "Description for Category 5"));

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AssetCategoryAdapter(categories, requireContext(), this);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            EditAssetCategoryPopup dialog = EditAssetCategoryPopup.newInstance(null, true);
            dialog.setListener(new EditAssetCategoryPopup.EditAssetCategoryListener() {
                @Override
                public void onSaveCategory(AssetCategory updatedCategory) {
                    categories.add(updatedCategory);
                    adapter.notifyItemInserted(categories.size() - 1);
                }

                @Override
                public void onDeleteCategory(AssetCategory categoryToDelete) {
                }

                @Override
                public void onCancel() {
                }
            });
            dialog.show(getChildFragmentManager(), "AddCategoryDialog");
        });

    }

    @Override
    public void onItemClick(AssetCategory category) {
        EditAssetCategoryPopup dialog = EditAssetCategoryPopup.newInstance(category, false);
        dialog.setListener(new EditAssetCategoryPopup.EditAssetCategoryListener() {
            @Override
            public void onSaveCategory(AssetCategory updatedCategory) {
                int index = getCategoryIndex(updatedCategory);
                categories.set(index, updatedCategory);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onDeleteCategory(AssetCategory categoryToDelete) {
                categories.remove(categoryToDelete);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
            }
        });
        dialog.show(getChildFragmentManager(), "EditCategoryDialog");
    }

    private int getCategoryIndex(AssetCategory category) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).equals(category)) {
                return i;
            }
        }
        return -1;
    }
}
