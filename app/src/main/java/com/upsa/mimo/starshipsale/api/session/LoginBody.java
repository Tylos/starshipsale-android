package com.upsa.mimo.starshipsale.api.session;

import com.google.gson.annotations.SerializedName;

public class LoginBody {

    @SerializedName("email") final String email;
    @SerializedName("password") final String password;

    public LoginBody(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
