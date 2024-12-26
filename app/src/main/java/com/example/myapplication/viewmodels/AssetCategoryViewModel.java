package com.example.myapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.utilities.JwtTokenUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetCategoryViewModel extends ViewModel {

    private final AssetCategoryService assetCategoryService;
    private final MutableLiveData<List<AssetCategory>> activeCategories = new MutableLiveData<>();
    private final MutableLiveData<List<AssetCategory>> pendingCategories = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);

    private final MutableLiveData<AssetCategory> selectedCategory = new MutableLiveData<>(null);

    public AssetCategoryViewModel() {
        this.assetCategoryService = new AssetCategoryService();
    }

    public LiveData<List<AssetCategory>> getActiveCategories() {
        return activeCategories;
    }

    public LiveData<List<AssetCategory>> getPendingCategories() {
        return pendingCategories;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<AssetCategory> getSelecetedAssetCategory(){return selectedCategory;}

    public void onCategoryChanged(){

    }

    public void loadCategories() {
        String token = JwtTokenUtil.getToken();
        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            isLoading.setValue(true);
            errorMessage.setValue(null);

            assetCategoryService.getActiveCategories(authHeader, new Callback<List<AssetCategory>>() {
                @Override
                public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                    isLoading.setValue(false);
                    if (response.isSuccessful() && response.body() != null) {
                        activeCategories.setValue(response.body());
                    } else {
                        errorMessage.setValue("Failed to load active categories");
                    }
                }

                @Override
                public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                    isLoading.setValue(false);
                    errorMessage.setValue(t.getMessage());
                }
            });

            assetCategoryService.getPendingCategories(authHeader, new Callback<List<AssetCategory>>() {
                @Override
                public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        pendingCategories.setValue(response.body());
                    } else {
                        errorMessage.setValue("Failed to load pending categories");
                    }
                }

                @Override
                public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                    errorMessage.setValue(t.getMessage());
                }
            });
        }
    }

    public void saveCategory(AssetCategory updatedCategory, boolean isAddMode, Callback<AssetCategory> callback) {
        String token = JwtTokenUtil.getToken();
        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            if (isAddMode) {
                assetCategoryService.createCategory(authHeader, updatedCategory, callback);
            } else {
                assetCategoryService.updateCategory(authHeader, updatedCategory.getId(), updatedCategory, callback);
            }
        }
    }

    public void deleteCategory(AssetCategory categoryToDelete, Callback<Void> callback) {
        String token = JwtTokenUtil.getToken();
        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;
            assetCategoryService.deleteCategory(authHeader, categoryToDelete.getId(), callback);
        }
    }

    public void approveCategory(AssetCategory category, Callback<Void> callback) {
        String token = JwtTokenUtil.getToken();
        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;
            assetCategoryService.approveCategory(authHeader, category.getId(), callback);
        }
    }
}
