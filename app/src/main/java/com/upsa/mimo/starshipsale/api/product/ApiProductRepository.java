package com.upsa.mimo.starshipsale.api.product;

import android.content.Context;

import com.upsa.mimo.starshipsale.api.ApiBuilder;
import com.upsa.mimo.starshipsale.domain.entities.Product;
import com.upsa.mimo.starshipsale.domain.repositories.ProductRepository;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class ApiProductRepository implements ProductRepository {

    private ProductApi productApi;

    public ApiProductRepository(Context context, String serverUrl) {
        this.productApi = new ApiBuilder<>(context, ProductApi.class, serverUrl).buildApiResource();
    }

    @Override
    public List<Product> getAll() throws IOException {
        final Response<List<Product>> response = productApi.getAll().execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException(String.valueOf(response.errorBody()));
        }
    }

    @Override
    public Product getById(String id) throws IOException {
        final Response<Product> response = productApi.getProduct(id).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException(String.valueOf(response.errorBody()));
        }
    }
}
