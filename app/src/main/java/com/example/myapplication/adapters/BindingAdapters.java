package com.example.myapplication.adapters;

import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.Role;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.utilities.JwtTokenUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BindingAdapters {

    @BindingAdapter(value = {"userType","userTypeAttrChanged"}, requireAll = false)
    public static void bindProfileType(Spinner spinner, Role selectedType, final InverseBindingListener listener) {
        if (spinner.getAdapter() == null) {
            ArrayAdapter<Role> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, Role.values());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

        if (selectedType != null) {
            int position = ((ArrayAdapter<Role>) spinner.getAdapter()).getPosition(selectedType);
            spinner.setSelection(position);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                listener.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                listener.onChange();
            }
        });
    }
    @InverseBindingAdapter(attribute = "userType")
    public static Role getUserType(Spinner spinner) {
        return (Role) spinner.getSelectedItem();
    }

    @BindingAdapter(value={"eventType","eventTypeAttrChanged"},requireAll = false)
    public static void bindEventType(Spinner spinner, Role selectedType, final InverseBindingListener listener) {
        if (spinner.getAdapter() == null) {
            ArrayAdapter<Role> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, Role.values());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

        if (selectedType != null) {
            int position = ((ArrayAdapter<Role>) spinner.getAdapter()).getPosition(selectedType);
            spinner.setSelection(position);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                listener.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                listener.onChange();
            }
        });
    }

    @InverseBindingAdapter(attribute = "eventType")
    public static Role getEventType(Spinner spinner) {
        return (Role) spinner.getSelectedItem();
    }

    @BindingAdapter(value = {"assetCategory", "assetCategoryAttrChanged"}, requireAll = false)
    public static void bindAssetCategory(Spinner spinner, AssetCategory selectedCategory, final InverseBindingListener listener) {
        AssetCategoryService service = new AssetCategoryService();

        // Check if Spinner is already set with an Adapter
        if (spinner.getAdapter() == null) {
            service.getActiveCategories(JwtTokenUtil.getToken(), new Callback<List<AssetCategory>>() {
                @Override
                public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                    if (response.body() != null) {
                        ArrayAdapter<AssetCategory> adapter = new ArrayAdapter<>(
                                spinner.getContext(),
                                android.R.layout.simple_spinner_item,
                                response.body()
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        // Set the selected category if it exists
                        if (selectedCategory != null) {
                            int position = adapter.getPosition(selectedCategory);
                            spinner.setSelection(position);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                    Log.d("Error", "Failed to load categories: " + t.getMessage());
                }
            });
        } else {
            // If adapter is already set, update the selection directly
            ArrayAdapter<AssetCategory> adapter = (ArrayAdapter<AssetCategory>) spinner.getAdapter();
            if (selectedCategory != null) {
                int position = adapter.getPosition(selectedCategory);
                spinner.setSelection(position);
            }
        }

        // Listen for changes in selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (listener != null) {
                    listener.onChange();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                if (listener != null) {
                    listener.onChange();
                }
            }
        });
    }

    @InverseBindingAdapter(attribute = "assetCategory")
    public static AssetCategory getAssetCategory(Spinner spinner) {
        return (AssetCategory) spinner.getSelectedItem();
    }

}
