package com.example.myapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.domain.Asset;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.Product;
import com.example.myapplication.domain.Utility;
import com.example.myapplication.domain.dto.SearchAssetRequest;
import com.example.myapplication.domain.dto.SearchEventsRequest;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.services.ProductService;
import com.example.myapplication.services.UtilityService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetViewModel extends ViewModel {
    private final ProductService productService;
    private final UtilityService utilityService;
    private final AssetCategoryService categoryService;

    private final MutableLiveData<List<Asset>> assetsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private final MutableLiveData<SearchAssetRequest> currentFilters = new MutableLiveData<>();

    public AssetViewModel() {
        this.productService = new ProductService();
        this.utilityService = new UtilityService();
        this.categoryService = new AssetCategoryService();
        this.currentFilters.setValue(new SearchAssetRequest());
    }

    public void setCurrentFilters(SearchAssetRequest request){
        this.currentFilters.setValue(request);
    }

    public LiveData<SearchAssetRequest> getCurrentFilters() {return currentFilters;}

    public LiveData<List<Asset>> getAssetsLiveData() {
        return assetsLiveData;
    }

    public LiveData<Boolean> getLoadingState() {
        return isLoading;
    }

    public void fetchAssets(String token) {
        isLoading.setValue(true);

        productService.getAllProducts(token, new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Asset> combinedAssets = new ArrayList<>(response.body());

                    utilityService.getAllUtilities(token, new Callback<List<Utility>>() {
                        @Override
                        public void onResponse(Call<List<Utility>> call, Response<List<Utility>> response) {
                            isLoading.setValue(false);
                            if (response.isSuccessful() && response.body() != null) {
                                combinedAssets.addAll(response.body());
                                resolveAssetCategories(token, combinedAssets);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Utility>> call, Throwable t) {
                            isLoading.setValue(false);
                            // Handle error
                        }
                    });
                } else {
                    isLoading.setValue(false);
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                isLoading.setValue(false);
                // Handle error
            }
        });
    }

    private void resolveAssetCategories(String token, List<Asset> assets) {
        final int[] updatedCount = {0};
        for (Asset asset : assets) {
            Log.d("Category ID", asset.getCategory());
            categoryService.getCategoryById(token, asset.getCategory(), new Callback<AssetCategory>() {
                @Override
                public void onResponse(Call<AssetCategory> call, Response<AssetCategory> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        asset.setType(response.body().getType());
                    }
                    updatedCount[0]++;
                    if (updatedCount[0] == assets.size()) {
                        assetsLiveData.setValue(new ArrayList<>(assets));
                    }
                }

                @Override
                public void onFailure(Call<AssetCategory> call, Throwable t) {
                    Log.e("API Failure", "Error occurred: " + t.getMessage());
                    updatedCount[0]++;
                    if (updatedCount[0] == assets.size()) {
                        assetsLiveData.setValue(new ArrayList<>(assets));
                    }
                }
            });
        }
    }
}

