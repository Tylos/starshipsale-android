package com.upsa.mimo.starshipsale.api.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.upsa.mimo.starshipsale.api.ApiBuilder;
import com.upsa.mimo.starshipsale.domain.entities.Session;

import java.io.IOException;

import retrofit2.Response;

public class SessionRepository {
    private static final String SHARED_PREFERENCES_API_TOKEN = "shared_preferences.api_token";

    private final SharedPreferences sharedPreferences;
    private SessionApi sessionApi;

    public SessionRepository(Context context, String serverUrl) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        this.sessionApi = new ApiBuilder<>(SessionApi.class, serverUrl).buildApiResource();
    }

    public Session login(String email, String password) throws IOException {
        final LoginBody body = new LoginBody(email, password);
        final Response<Session> response = sessionApi.login(body).execute();
        if (response.isSuccessful()) {
            final Session session = response.body();
            sharedPreferences
                    .edit()
                    .putString(SHARED_PREFERENCES_API_TOKEN, session.getToken())
                    .apply();
            return session;
        } else {
            throw new IOException(String.valueOf(response.code()));
        }
    }

    public Session getCurrentSession() {
        return recoverSession();
    }

    private Session recoverSession() {
        final String token = sharedPreferences.getString(SHARED_PREFERENCES_API_TOKEN, null);
        if (token != null) {
            return new Session(token);
        } else {
            return null;
        }
    }
}
