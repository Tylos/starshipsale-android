package com.upsa.mimo.starshipsale.api.cart;

import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartApi {

    @GET("/api/cart")
    Call<List<Product>> list();

    @POST("/api/cart/{productId}")
    Call<Product> addToCart(@Path("productId")String productId, @Body String emptyBody);

    @DELETE("/api/cart/{productId}")
    Call<Product> removeFromCart(@Path("productId")String productId);
}
