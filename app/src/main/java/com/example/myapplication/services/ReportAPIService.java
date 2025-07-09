package com.example.myapplication.services;

import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.PagedResponse;
import com.example.myapplication.domain.dto.CreateReportRequest;
import com.example.myapplication.domain.dto.ReportResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReportAPIService {
    @POST("reports")
    Call<ApiResponse> createReport(
        @Body CreateReportRequest createReportRequest
    );

    @GET("reports")
    Call<PagedResponse<ReportResponse>> getReports (@Query("page") int page,
                                                    @Query("size") int size);

    @PUT("reports/suspend")
    Call<ApiResponse> suspendUser(@Body String id);
}
