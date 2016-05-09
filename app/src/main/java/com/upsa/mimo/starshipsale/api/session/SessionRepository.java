package com.upsa.mimo.starshipsale.api.session;

import android.content.Context;

import com.upsa.mimo.starshipsale.api.ApiBuilder;
import com.upsa.mimo.starshipsale.domain.entities.Session;

import java.io.IOException;

import retrofit2.Response;

public class SessionRepository {

    private SessionApi sessionApi;

    public SessionRepository(Context context, String serverUrl) {
        this.sessionApi = new ApiBuilder<>(context, SessionApi.class, serverUrl).buildApiResource();
    }

    public Session login(String email, String password) throws IOException {
        final LoginBody body = new LoginBody(email, password);
        final Response<Session> response = sessionApi.login(body).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException(String.valueOf(response.code()));
        }
    }
}
