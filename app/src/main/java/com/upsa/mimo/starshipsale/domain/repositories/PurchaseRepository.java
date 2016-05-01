package com.upsa.mimo.starshipsale.domain.repositories;

import java.io.IOException;

public interface PurchaseRepository {

    void purchase(String id) throws IOException;

    void purchaseCart() throws IOException;
}
