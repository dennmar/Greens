package com.example.dennis.greens;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DatabaseService {
    @POST("auth/login/")
    Call<JsonObject> login(@Body JsonObject jsonObj);

    @POST("auth/refresh/")
    Call<JsonObject> refresh(@Header("Authorization") String refreshToken,
            @Body JsonObject jsonObj);

    @POST("user/")
    Call<JsonObject> createUser(@Body JsonObject jsonObj);

    @GET("user/{user_id}/expense/")
    Call<JsonObject> getExpenses(
            @Header("Authorization") String accessToken,
            @Path("user_id") int userId
    );
}
