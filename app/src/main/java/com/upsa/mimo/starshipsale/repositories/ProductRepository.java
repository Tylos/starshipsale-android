package com.upsa.mimo.starshipsale.repositories;


import com.upsa.mimo.starshipsale.domain.entities.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> getAll();

    Product getById(Long id);

}
