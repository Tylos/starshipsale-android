package com.upsa.mimo.starshipsale.domain.repositories;

import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.io.IOException;
import java.util.List;

public interface CartRepository {
    List<Product> getAll() throws IOException;

    Product favorite(Long id) throws IOException;

    Product unfavorite(Long id) throws IOException;
}
