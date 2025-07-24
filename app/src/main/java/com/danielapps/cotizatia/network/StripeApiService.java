package com.danielapps.cotizatia.network;
import com.danielapps.cotizatia.model.PlataOnline;
import com.danielapps.cotizatia.model.StripeSessionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface StripeApiService {
    @POST("api/create-stripe-session")
    Call<StripeSessionResponse> createStripeSession(@Body PlataOnline plata);
}

