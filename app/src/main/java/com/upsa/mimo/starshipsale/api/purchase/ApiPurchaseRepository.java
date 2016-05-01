package com.upsa.mimo.starshipsale.api.purchase;

import com.upsa.mimo.starshipsale.api.ApiBuilder;
import com.upsa.mimo.starshipsale.api.cart.CartApi;
import com.upsa.mimo.starshipsale.domain.entities.Product;
import com.upsa.mimo.starshipsale.domain.entities.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class ApiPurchaseRepository implements com.upsa.mimo.starshipsale.domain.repositories.PurchaseRepository {

    private final CartApi cartApi;
    private final PurchaseApi productApi;

    public ApiPurchaseRepository(String serverUrl, Session session) {
        this.productApi = new ApiBuilder<>(PurchaseApi.class, serverUrl, session).buildApiResource();
        this.cartApi = new ApiBuilder<>(CartApi.class, serverUrl, session).buildApiResource();
    }


    @Override
    public void purchase(final String id) throws IOException {
        final ArrayList<String> ids = new ArrayList<>();
        ids.add(id);
        PurchaseBody body = new PurchaseBody(ids);
        final Response<Void> response = productApi.purchase(body).execute();
        if (response.isSuccessful()) {
        } else {
            throw new IOException(String.valueOf(response.code()));
        }
    }

    @Override
    public void purchaseCart() throws IOException {
        final Response<List<Product>> cartResponse = cartApi.list().execute();
        final ArrayList<String> ids = new ArrayList<>();
        if (cartResponse.isSuccessful()) {
            for (Product product : cartResponse.body()) {
                ids.add(product.getId());
            }
        } else {
            throw new IOException(String.valueOf(cartResponse.code()));
        }

        PurchaseBody body = new PurchaseBody(ids);
        final Response<Void> response = productApi.purchase(body).execute();
        if (response.isSuccessful()) {
        } else {
            throw new IOException(String.valueOf(response.code()));
        }
    }
}
