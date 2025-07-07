package com.example.myapplication.services;

import com.example.myapplication.domain.Product;
import com.example.myapplication.domain.Review;
import com.example.myapplication.utilities.RetrofitClient;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import java.util.List;

public class ProductService {

    private final ProductAPIService apiService;

    public ProductService() {
        apiService = RetrofitClient.getRetrofitInstance().create(ProductAPIService.class);
    }

    public void getAllProducts(String token, Callback<List<Product>> callback) {
        Call<List<Product>> call = apiService.getAllProducts(token);
        call.enqueue(callback);
    }

    public void getProductById(String token, String id, Callback<Product> callback) {
        Call<Product> call = apiService.getProductById(token, id);
        call.enqueue(callback);
    }

    public void createProduct(String token, String name, String description, String category, String providerId, double price, double discount,
                              boolean visible, boolean available, List<MultipartBody.Part> images, String suggestedCategoryName, String suggestedCategoryDesc, Callback<Product> callback) {

        RequestBody namePart = RequestBody.create(MultipartBody.FORM, name);
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, description);
        RequestBody pricePart = RequestBody.create(MultipartBody.FORM, String.valueOf(price));
        RequestBody discountPart = RequestBody.create(MultipartBody.FORM, String.valueOf(discount));
        RequestBody visiblePart = RequestBody.create(MultipartBody.FORM, String.valueOf(visible));
        RequestBody availablePart = RequestBody.create(MultipartBody.FORM, String.valueOf(available));
        RequestBody suggestedCategoryNamePart = RequestBody.create(MultipartBody.FORM,
                suggestedCategoryName != null ? suggestedCategoryName : "");
        RequestBody suggestedCategoryDescPart = RequestBody.create(MultipartBody.FORM,
                suggestedCategoryDesc != null ? suggestedCategoryDesc : "");
        RequestBody categoryPart = RequestBody.create(MultipartBody.FORM, category);
        RequestBody providerIdPart = RequestBody.create(MultipartBody.FORM, providerId);
        Call<Product> call = apiService.createProduct(token, namePart, descriptionPart, pricePart, discountPart,
                visiblePart, availablePart, images, suggestedCategoryNamePart, suggestedCategoryDescPart, categoryPart, providerIdPart);
        call.enqueue(callback);
    }

    public void updateProduct(String token, String id, String name, String description, double price, double discount,
                              boolean visible, boolean available, List<MultipartBody.Part> images, Callback<Object> callback) {

        RequestBody namePart = RequestBody.create(MultipartBody.FORM, name);
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, description);
        RequestBody pricePart = RequestBody.create(MultipartBody.FORM, String.valueOf(price));
        RequestBody discountPart = RequestBody.create(MultipartBody.FORM, String.valueOf(discount));
        RequestBody visiblePart = RequestBody.create(MultipartBody.FORM, String.valueOf(visible));
        RequestBody availablePart = RequestBody.create(MultipartBody.FORM, String.valueOf(available));

        Call<Object> call = apiService.updateProduct(token, id, namePart, descriptionPart, pricePart, discountPart,
                visiblePart, availablePart, images);
        call.enqueue(callback);
    }

    public void deleteProduct(String token, String id, Callback<Void> callback) {
        Call<Void> call = apiService.deleteProduct(token, id);
        call.enqueue(callback);
    }

    public void addProductToFavorites(String token, String id, Callback<String> callback) {
        Call<String> call = apiService.addProductToFavorites(token, id);
        call.enqueue(callback);
    }

    public void removeProductFromFavorites(String token, String id, Callback<String> callback) {
        Call<String> call = apiService.removeProductFromFavorites(token, id);
        call.enqueue(callback);
    }

    public void submitReview(String token, String productId, RequestBody reviewData, Callback<String> callback) {
        Call<String> call = apiService.submitReview(token, productId, reviewData);
        call.enqueue(callback);
    }
}
