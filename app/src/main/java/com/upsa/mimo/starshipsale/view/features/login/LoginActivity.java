package com.upsa.mimo.starshipsale.view.features.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import com.upsa.mimo.starshipsale.BuildConfig;
import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.api.session.SessionRepository;
import com.upsa.mimo.starshipsale.domain.entities.Session;
import com.upsa.mimo.starshipsale.view.MainActivity;

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
        emailInput = (EditText) findViewById(R.id.email_input);
        passwordInput = (EditText) findViewById(R.id.password_input);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.cta_view_switcher);
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        new Pair<>(
                                emailInput.getText().toString(),
                                passwordInput.getText().toString()
                        ));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class LoginAsyncTask extends AsyncTask<Pair<String, String>, Void, Session> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            viewSwitcher.showNext();
        }

        @Override
        protected Session doInBackground(Pair<String, String>... params) {
            try {
                return new SessionRepository(LoginActivity.this, BuildConfig.SERVER_REST_URL)
                        .login(
                                params[0].first,
                                params[0].second);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Session session) {
            super.onPostExecute(session);
            viewSwitcher.showPrevious();
            if (session != null) {
                MainActivity.launch(LoginActivity.this);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), R.string.generic_error, Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
