package com.upsa.mimo.starshipsale.api.favorite;

import com.upsa.mimo.starshipsale.api.ApiBuilder;
import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class ApiFavoritesRepository implements com.upsa.mimo.starshipsale.domain.repositories.FavoritesRepository {

    private FavoritesApi favoritesApi;

    public ApiFavoritesRepository(String serverUrl) {
        this.favoritesApi = new ApiBuilder<>(FavoritesApi.class, serverUrl).buildApiResource();
    }

    @Override
    public List<Product> getAll() throws IOException {
        final Response<List<Product>> response = favoritesApi.list().execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException(String.valueOf(response.errorBody()));
        }
    }

    @Override
    public Product favorite(Long id) throws IOException {
        final Response<Product> response = favoritesApi.favorite(id).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException(String.valueOf(response.errorBody()));
        }
    }

    @Override
    public Product unfavorite(Long id) throws IOException {
        final Response<Product> response = favoritesApi.unfavorite(id).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException(String.valueOf(response.errorBody()));
        }
    }
}
