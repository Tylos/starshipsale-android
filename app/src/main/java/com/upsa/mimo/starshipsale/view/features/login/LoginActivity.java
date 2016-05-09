package com.upsa.mimo.starshipsale.view.features.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import com.upsa.mimo.starshipsale.BuildConfig;
import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.api.session.SessionRepository;
import com.upsa.mimo.starshipsale.domain.entities.Session;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText passwordInput;
    private EditText emailInput;
    private ViewSwitcher viewSwitcher;

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        configureView();
    }

    private void configureView() {
        emailInput = (EditText) findViewById(R.id.email_input);
        passwordInput = (EditText) findViewById(R.id.password_input);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.cta_view_switcher);
        //noinspection ConstantConditions
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        emailInput.getText().toString(), passwordInput.getText().toString());
            }
        });
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, Bundle> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            viewSwitcher.showNext();
        }

        @Override
        protected Bundle doInBackground(String... params) {
            try {
                final Session session = new SessionRepository(LoginActivity.this, BuildConfig.SERVER_REST_URL)
                        .login(params[0], params[1]);

                return new Bundle();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bundle result) {
            super.onPostExecute(result);
            viewSwitcher.showPrevious();
            if (result != null) {
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), R.string.generic_error, Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
