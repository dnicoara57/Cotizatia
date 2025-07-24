package com.danielapps.cotizatia.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://racheta-hateg.nicalemardan.ro/";
    private static Retrofit retrofit = null;

    public static StripeApiService getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(StripeApiService.class);
    }
}

