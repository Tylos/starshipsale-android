package com.upsa.mimo.starshipsale.api.session;

import com.upsa.mimo.starshipsale.domain.entities.Session;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SessionApi {

    @POST("session/login")
    Call<Session> login(@Body LoginBody info);
}
