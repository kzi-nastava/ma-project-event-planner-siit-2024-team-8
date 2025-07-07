package com.example.myapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.domain.Asset;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.PagedResponse;
import com.example.myapplication.domain.Product;
import com.example.myapplication.domain.Utility;
import com.example.myapplication.domain.dto.AssetResponse;
import com.example.myapplication.domain.dto.SearchAssetRequest;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.services.ClientUtils;
import com.example.myapplication.services.ProductService;
import com.example.myapplication.services.UtilityService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetViewModel extends ViewModel {
    private final ProductService productService;
    private final UtilityService utilityService;
    private final AssetCategoryService categoryService;

    private final MutableLiveData<List<Asset>> assetsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private final MutableLiveData<List<AssetResponse>> currentAssets = new MutableLiveData<>();

    private final MutableLiveData<SearchAssetRequest> currentFilters = new MutableLiveData<>();

    private final MutableLiveData<List<AssetResponse>> top5Assets = new MutableLiveData<>();

    private MutableLiveData<Long> totalElements = new MutableLiveData<>();

    private MutableLiveData<Integer> totalPages = new MutableLiveData<>();

    public AssetViewModel() {
        this.productService = new ProductService();
        this.utilityService = new UtilityService();
        this.categoryService = new AssetCategoryService();
        this.currentFilters.setValue(new SearchAssetRequest());
    }

    public void setCurrentFilters(SearchAssetRequest request){
        this.currentFilters.setValue(request);
    }

    public LiveData<List<AssetResponse>> getCurrentAssets(){
        return currentAssets;
    }

    public LiveData<Long> getTotalElements() {return totalElements;}
    public LiveData<Integer> getTotalPages() {return totalPages;}

    public LiveData<SearchAssetRequest> getCurrentFilters() {return currentFilters;}

    public LiveData<List<Asset>> getAssetsLiveData() {
        return assetsLiveData;
    }

    public LiveData<Boolean> getLoadingState() {
        return isLoading;
    }

    public LiveData<List<AssetResponse>> getTop5Assets() { return top5Assets;}

    public void fetchAssets(String token) {
        isLoading.setValue(true);

        productService.getAllProducts(token, new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();
                    for (Product product : products) {
                        Log.d("PRODUCT_FETCHED", "Product ID: " + product.getId());
                    }
                    List<Asset> combinedAssets = new ArrayList<>(response.body());

                    utilityService.getAllUtilities(token, new Callback<List<Utility>>() {
                        @Override
                        public void onResponse(Call<List<Utility>> call, Response<List<Utility>> response) {
                            isLoading.setValue(false);
                            if (response.isSuccessful() && response.body() != null) {
                                combinedAssets.addAll(response.body());
                                resolveAssetCategories(token, combinedAssets);
                                Log.d("ProductService", "Fetched products: " + response.body().toString());
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

    public void filterAssets(int currentPage,int pageSize){
        SearchAssetRequest request = currentFilters.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;


        Integer priceLow = (request.getPriceLow() != null && request.getPriceLow() > 0) ? request.getPriceLow() : null;
        Integer priceHigh = (request.getPriceHigh() != null && request.getPriceHigh() > 0) ? request.getPriceHigh() : null;
        Integer gradeLow = (request.getGradeLow() != null && request.getGradeLow() > 0) ? request.getGradeLow() : null;
        Integer gradeHigh = (request.getGradeHigh() != null && request.getGradeHigh() > 0) ? request.getGradeHigh() : null;

        Log.d("FILTERS", "Filtering assets with values: \n" +
                "Name: " + request.getName() + "\n" +
                "Categories: " + request.getAssetCategories() + "\n" +
                "Type: " + request.getAssetType() + "\n" +
                "PriceLow: " + priceLow + "\n" +
                "PriceHigh: " + priceHigh + "\n" +
                "GradeLow: " + gradeLow + "\n" +
                "GradeHigh: " + gradeHigh + "\n" +
                "SortBy: " + request.getSortBy() + "\n" +
                "SortOrder: " + request.getSortOrder() + "\n" +
                "Available: " + request.getAvailable() + "\n" +
                "Owner: " + request.getOwner());

        Call<PagedResponse<AssetResponse>> call = ClientUtils.assetAPIService.filterAssets(
                currentPage,
                pageSize,
                request.getName(),
                request.getAssetCategories(),
                request.getAssetType(),
                priceLow,
                priceHigh,
                gradeLow,
                gradeHigh,
                request.getSortBy(),
                request.getSortOrder(),
                request.getAvailable(),
                request.getOwner()
        );
        call.enqueue(new Callback<PagedResponse<AssetResponse>>() {
            @Override
            public void onResponse(Call<PagedResponse<AssetResponse>> call, Response<PagedResponse<AssetResponse>> response) {
                if (response.body() == null){return;}
                List<AssetResponse> assets = response.body().getContent();
                Log.d("ASSET_FILTER_RESULT", "Total assets fetched: " + assets.size());
                for (AssetResponse asset : assets) {
                    Log.d("ASSET_ID", "Filtered asset ID: " + asset.getId());
                }
                currentAssets.setValue(response.body().getContent());
                totalElements.setValue(response.body().getTotalElements());
                totalPages.setValue(response.body().getTotalPages());
            }

            @Override
            public void onFailure(Call<PagedResponse<AssetResponse>> call, Throwable t) {
                Log.d("error fetching events", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public void loadTopAssets(){
        Call<List<AssetResponse>> call = ClientUtils.assetAPIService.getTop5Assets();
        call.enqueue(new Callback<List<AssetResponse>>() {
            @Override
            public void onResponse(Call<List<AssetResponse>> call, Response<List<AssetResponse>> response) {
                if (response.isSuccessful()){
                    top5Assets.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<AssetResponse>> call, Throwable t) {
                Log.d("ERROR LOADING TOP ASSETS" , t.getMessage());
            }
        });
    }
}

