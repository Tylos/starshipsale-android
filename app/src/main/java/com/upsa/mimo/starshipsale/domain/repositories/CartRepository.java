package com.upsa.mimo.starshipsale.domain.repositories;

import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.io.IOException;
import java.util.List;

public interface CartRepository {
    List<Product> getAll() throws IOException;

    Product addToCart(String id) throws IOException;

    Product removeFromCart(String id) throws IOException;
}
