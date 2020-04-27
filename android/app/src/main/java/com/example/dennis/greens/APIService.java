package com.example.dennis.greens;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    @POST("auth/login/")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<JsonObject> login(@Body JsonObject jsonObj);

    @POST("auth/refresh/")
    @Headers("Accept: application/json")
    Call<JsonObject> refresh(@Header("Authorization") String refreshToken);

    @POST("user/")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<JsonObject> createUser(@Body JsonObject jsonObj);

    @GET("user/{user_id}/expense/")
    @Headers("Accept: application/json")
    Call<JsonObject> getExpenses(
            @Header("Authorization") String accessToken,
            @Path("user_id") int userId
    );
}
