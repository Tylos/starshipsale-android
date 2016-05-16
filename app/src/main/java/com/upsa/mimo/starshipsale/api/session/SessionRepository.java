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

    /**
     * Register a new account in StarshipSale
     *
     * @param email             user email - must be unique
     * @param password          user password
     * @throws IOException      if there is any error
     */
    public void register(String email, String password) throws IOException {
        // Surprise! No different implementation here as api is not prepared but methods are
        // segregated for exercises purposes
        doLogin(email, password);
    }

    /**
     * Logs the user in StarshipSale using arguments as credentials
     *
     * @param email             user email
     * @param password          user password
     * @throws IOException
     */
    public void login(String email, String password) throws IOException {
        doLogin(email, password);
    }

    private Session doLogin(String email, String password) throws IOException {
        final LoginBody body = new LoginBody(email, password);
        final Response<Session> response = sessionApi.login(body).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException(String.valueOf(response.code()));
        }
    }
}
