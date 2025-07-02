package com.example.myapplication.services;

import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.dto.CreateReportRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReportAPIService {
    @POST
    Call<ApiResponse> createReport(
        @Body CreateReportRequest createReportRequest
    );
}
