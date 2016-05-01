package com.upsa.mimo.starshipsale.api.purchase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PurchaseApi {

    @POST("/api/purchase")
    Call<Void> purchase(@Body PurchaseBody body);
}
