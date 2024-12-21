package com.example.myapplication.services;

import com.example.myapplication.domain.Utility;
import com.example.myapplication.utilities.RetrofitClient;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UtilityService {

    private final UtilityAPIService apiService;

    public UtilityService() {
        apiService = RetrofitClient.getRetrofitInstance().create(UtilityAPIService.class);
    }

    public void getAllUtilities(String token, Callback<List<Utility>> callback) {
        Call<List<Utility>> call = apiService.getAllUtilities(token);
        call.enqueue(callback);
    }

    public void getUtilityById(String token, String id, Callback<Utility> callback) {
        Call<Utility> call = apiService.getUtilityById(token, id);
        call.enqueue(callback);
    }

    public void createUtility(String token, String name, String description, String category, String providerId, double price, double discount,
                              boolean visible, boolean available, int duration, String reservationTerm,
                              String cancellationTerm, boolean manuelConfirmation, List<MultipartBody.Part> images,
                              String suggestedCategoryName, String suggestedCategoryDesc, Callback<Utility> callback) {

        RequestBody namePart = RequestBody.create(MultipartBody.FORM, name);
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, description);
        RequestBody pricePart = RequestBody.create(MultipartBody.FORM, String.valueOf(price));
        RequestBody discountPart = RequestBody.create(MultipartBody.FORM, String.valueOf(discount));
        RequestBody visiblePart = RequestBody.create(MultipartBody.FORM, String.valueOf(visible));
        RequestBody availablePart = RequestBody.create(MultipartBody.FORM, String.valueOf(available));
        RequestBody durationPart = RequestBody.create(MultipartBody.FORM, String.valueOf(duration));
        RequestBody reservationTermPart = RequestBody.create(MultipartBody.FORM, reservationTerm);
        RequestBody cancellationTermPart = RequestBody.create(MultipartBody.FORM, cancellationTerm);
        RequestBody manuelConfirmationPart = RequestBody.create(MultipartBody.FORM, String.valueOf(manuelConfirmation));
        RequestBody suggestedCategoryNamePart = RequestBody.create(MultipartBody.FORM,
                suggestedCategoryName != null ? suggestedCategoryName : "");
        RequestBody suggestedCategoryDescPart = RequestBody.create(MultipartBody.FORM,
                suggestedCategoryDesc != null ? suggestedCategoryDesc : "");
        RequestBody categoryPart = RequestBody.create(MultipartBody.FORM, category);
        RequestBody providerIdPart = RequestBody.create(MultipartBody.FORM, providerId);

        Call<Utility> call = apiService.createUtility(token, namePart, descriptionPart, pricePart, discountPart,
                visiblePart, availablePart, durationPart, reservationTermPart, cancellationTermPart,
                manuelConfirmationPart, images, suggestedCategoryNamePart, suggestedCategoryDescPart,
                categoryPart, providerIdPart);
        call.enqueue(callback);
    }


    public void updateUtility(String token, String id, String name, String description, double price, double discount,
                              boolean visible, boolean available, int duration, String reservationTerm,
                              String cancellationTerm, boolean manuelConfirmation, List<MultipartBody.Part> images,
                              Callback<Utility> callback) {

        RequestBody namePart = RequestBody.create(MultipartBody.FORM, name);
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, description);
        RequestBody pricePart = RequestBody.create(MultipartBody.FORM, String.valueOf(price));
        RequestBody discountPart = RequestBody.create(MultipartBody.FORM, String.valueOf(discount));
        RequestBody visiblePart = RequestBody.create(MultipartBody.FORM, String.valueOf(visible));
        RequestBody availablePart = RequestBody.create(MultipartBody.FORM, String.valueOf(available));
        RequestBody durationPart = RequestBody.create(MultipartBody.FORM, String.valueOf(duration));
        RequestBody reservationTermPart = RequestBody.create(MultipartBody.FORM, reservationTerm);
        RequestBody cancellationTermPart = RequestBody.create(MultipartBody.FORM, cancellationTerm);
        RequestBody manuelConfirmationPart = RequestBody.create(MultipartBody.FORM, String.valueOf(manuelConfirmation));

        Call<Utility> call = apiService.updateUtility(token, id, namePart, descriptionPart, pricePart, discountPart,
                visiblePart, availablePart, durationPart, reservationTermPart, cancellationTermPart,
                manuelConfirmationPart, images);
        call.enqueue(callback);
    }

    public void deleteUtility(String token, String id, Callback<Void> callback) {
        Call<Void> call = apiService.deleteUtility(token, id);
        call.enqueue(callback);
    }

    public void addUtilityToFavorites(String token, String id, Callback<String> callback) {
        Call<String> call = apiService.addUtilityToFavorites(token, id);
        call.enqueue(callback);
    }

    public void removeUtilityFromFavorites(String token, String id, Callback<String> callback) {
        Call<String> call = apiService.removeUtilityFromFavorites(token, id);
        call.enqueue(callback);
    }
}
