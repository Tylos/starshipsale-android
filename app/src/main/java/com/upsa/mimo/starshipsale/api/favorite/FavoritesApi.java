package com.upsa.mimo.starshipsale.api.favorite;

import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoritesApi {

    @GET("favorites")
    Call<List<Product>> list();

    @POST("favorites/{productId}")
    Call<Product> favorite(@Path("productId")String productId);

    @DELETE("favorites/{productId}")
    Call<Product> unfavorite(@Path("productId")String productId);
}
