package com.upsa.mimo.starshipsale.api.purchase;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PurchaseBody {

    @SerializedName("products")
    private final List<String> ids;

    public PurchaseBody(List<String> ids) {
        this.ids = ids;
    }
}
