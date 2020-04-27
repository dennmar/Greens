package com.example.dennis.greens;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static RestClient instance;
    private static Context context;
    private APIService dbService;

    private RestClient(Context ctx) {
        context = ctx;
        String url = context.getResources().getString(R.string.api_root_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dbService = retrofit.create(APIService.class);
    }

    public static synchronized RestClient getInstance(Context ctx) {
        if (instance == null) {
            return new RestClient(ctx);
        }

        return instance;
    }

    public APIService getAPIService() {
        return dbService;
    }
}
