package com.upsa.mimo.starshipsale.view.features.login;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
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

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText passwordInput;
    private EditText emailInput;
    private ViewSwitcher viewSwitcher;

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        configureView();
        initAuthentication();
    }

    private void initAuthentication() {
        mAccountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    @Override
    public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
        super.finish();
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
            final Bundle bundle = new Bundle();
            try {
                final String userName = params[0];
                final String userPassword = params[1];
                final String accountType = getString(R.string.account_type);

                new SessionRepository(LoginActivity.this, BuildConfig.SERVER_REST_URL).register(userName, userPassword);

                Account account = new Account(userName, accountType);
                AccountManager accountManager = AccountManager.get(getApplicationContext());
                accountManager.addAccountExplicitly(account, userPassword, null);

                bundle.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                return bundle;
            } catch (IOException e) {
                bundle.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_NETWORK_ERROR);
                bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "Something went wrong " + e.getMessage());
                return bundle;
            }
        }

        @Override
        protected void onPostExecute(Bundle result) {
            super.onPostExecute(result);
            viewSwitcher.showPrevious();
            if (result.containsKey(AccountManager.KEY_ACCOUNT_NAME)) {
                setAccountAuthenticatorResult(result);
                setResult(RESULT_OK);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        result.getString(AccountManager.KEY_ERROR_MESSAGE),
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}