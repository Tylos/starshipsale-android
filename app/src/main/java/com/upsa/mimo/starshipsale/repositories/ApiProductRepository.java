package com.upsa.mimo.starshipsale.repositories;

import com.upsa.mimo.starshipsale.api.ApiBuilder;
import com.upsa.mimo.starshipsale.api.product.ProductApi;
import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiProductRepository implements ProductRepository {

    private ProductApi productApi;
    private List<Product> allProducts;

    public ApiProductRepository(String serverUrl) {
        this.productApi = new ApiBuilder<>(ProductApi.class, serverUrl).buildApiResource();
    }

    @Override
    public List<Product> getAll() {
        productApi.getAll().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                allProducts = response.body();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
        return null;
    }

    @Override
    public Product getById(Long id) {
        return null;
    }
}
