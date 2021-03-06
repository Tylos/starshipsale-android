package com.upsa.mimo.starshipsale.domain.repositories;

import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.io.IOException;
import java.util.List;

public interface FavoritesRepository {

    List<Product> getAll() throws IOException;

    Product favorite(String id) throws IOException;

    Product unfavorite(String id) throws IOException;
}
