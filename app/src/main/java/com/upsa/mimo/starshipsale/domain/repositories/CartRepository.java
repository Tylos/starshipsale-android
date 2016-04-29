package com.upsa.mimo.starshipsale.domain.repositories;

import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.io.IOException;
import java.util.List;

public interface CartRepository {
    List<Product> getAll() throws IOException;

    Product addToCart(Long id) throws IOException;

    Product removeFromCart(Long id) throws IOException;
}
