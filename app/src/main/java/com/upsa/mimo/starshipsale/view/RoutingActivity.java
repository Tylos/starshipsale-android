package com.upsa.mimo.starshipsale.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.upsa.mimo.starshipsale.BuildConfig;
import com.upsa.mimo.starshipsale.api.session.SessionRepository;
import com.upsa.mimo.starshipsale.domain.entities.Session;
import com.upsa.mimo.starshipsale.view.features.login.LoginActivity;

public class RoutingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(isDefaultIntent()) {
            final Session currentSession = new SessionRepository(this, BuildConfig.SERVER_REST_URL).getCurrentSession();
            handleSession(currentSession);
        } else {
            // TODO proper routing based in intents
        }
    }

    private void handleSession(Session currentSession) {
        if (currentSession != null) {
            MainActivity.launch(this);
        } else {
            LoginActivity.launch(this);
        }
        finish();
    }

    private boolean isDefaultIntent() {
        return getIntent().getAction().equals(Intent.ACTION_MAIN) &&
                getIntent().getCategories().contains(Intent.CATEGORY_LAUNCHER);
    }
}
