package com.example.token_consumer_app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetToken().execute(AccountManager.get(this));
    }

    private void setTokenStatus(String message) {
        TextView tokenStatus = findViewById(R.id.token_status);
        tokenStatus.setText(message);
    }

    private class GetToken extends AsyncTask<AccountManager, Void, String>  {

        @Override
        protected String doInBackground(AccountManager... accountManagers) {
            return requestToken(accountManagers[0]);
        }

        @Override
        protected void onPostExecute(String status) {
            setTokenStatus(status);
        }

        private String requestToken(AccountManager accountManager) {
            Account[] acc = accountManager.getAccountsByType("com.mimo.starshipsale");
            if (acc.length > 0) {
                // possibly ask user for permission to obtain token
                String access = null;
                try {
                    access = accountManager.blockingGetAuthToken(acc[0], "access_token", true);
                } catch (AuthenticatorException e) {
                    return "AuthenticatorException";
                } catch (IOException e) {
                    return "IOException";
                } catch (OperationCanceledException e) {
                    return "OperationCanceledException";
                }
                return "Token found: " + access;
            } else {
                return "No account found";
            }
        }
    }
}
