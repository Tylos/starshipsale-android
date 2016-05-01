package com.upsa.mimo.starshipsale.domain.entities;

import com.google.gson.annotations.SerializedName;

public class Session {

    @SerializedName("token") String token;

    public Session(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
