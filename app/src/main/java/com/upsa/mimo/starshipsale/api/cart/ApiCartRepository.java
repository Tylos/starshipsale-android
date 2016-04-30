package com.upsa.mimo.starshipsale.api.cart;

import com.upsa.mimo.starshipsale.api.ApiBuilder;
import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class ApiCartRepository implements com.upsa.mimo.starshipsale.domain.repositories.CartRepository {

    private CartApi cartApi;

    public ApiCartRepository(String serverUrl) {
        this.cartApi = new ApiBuilder<>(CartApi.class, serverUrl).buildApiResource();
    }

    @Override
    public List<Product> getAll() throws IOException {
        final Response<List<Product>> response = cartApi.list().execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException(String.valueOf(response.code()));
        }
    }

    @Override
    public Product addToCart(String id) throws IOException {
        final Response<Product> response = cartApi.addToCart(id).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException(String.valueOf(response.code()));
        }
    }

    @Override
    public Product removeFromCart(String id) throws IOException {
        final Response<Product> response = cartApi.removeFromCart(id).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException(String.valueOf(response.code()));
        }
    }
}
