package com.example.project;

import retrofit2.Call;
import retrofit2.http.GET;

public interface YugiohApi {
    @GET("api/v7/cardinfo.php")
    Call<RestYugiohResponse> getYugiohResponse();
}
