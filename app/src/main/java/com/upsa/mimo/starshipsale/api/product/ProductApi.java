package com.upsa.mimo.starshipsale.api.product;

import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ProductApi {

    @GET("/api/products")
    Call<List<Product>> getAll();

    @GET("/api/products/{productId}")
    Call<Product> getProduct(@Path("productId") Long productId);

}