package com.upsa.mimo.starshipsale.domain.repositories;


import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.io.IOException;
import java.util.List;

public interface ProductRepository {

    List<Product> getAll() throws IOException;

    Product getById(String id) throws IOException;
}
