package com.example.dennis.greens;


import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A singleton client that makes requests to the REST API.
 */
public class RestClient {
    private static RestClient instance;
    private static Context context;
    private APIService apiService;

    /**
     * Initialize the client that will make requests to the REST API.
     *
     * @param ctx the context for the activity
     */
    private RestClient(Context ctx) {
        context = ctx;
        String url = context.getResources().getString(R.string.api_root_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(APIService.class);
    }

    /**
     * Get the single instance of the REST API client.
     *
     * @param ctx the context for the activity
     * @return the REST API client
     */
    public static synchronized RestClient getInstance(Context ctx) {
        if (instance == null) {
            return new RestClient(ctx);
        }

        return instance;
    }

    /**
     * Get the API service that is associated with the client.
     *
     * @return the API service
     */
    public APIService getAPIService() {
        return apiService;
    }
}
