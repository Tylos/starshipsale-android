package com.upsa.mimo.starshipsale.view.features.login;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import com.upsa.mimo.starshipsale.R;
import com.upsa.mimo.starshipsale.view.features.authentication.Authenticator;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText passwordInput;
    private EditText emailInput;
    private ViewSwitcher viewSwitcher;

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;
    private Account account = null;

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        final Bundle extras = getIntent().getExtras();
        if (containsAccount(extras)) {
            account = new Account(
                    extras.getString(AccountManager.KEY_ACCOUNT_NAME),
                    extras.getString(AccountManager.KEY_ACCOUNT_TYPE)
            );
        }
        configureView();
        initAuthentication();
    }

    private boolean containsAccount(Bundle extras) {
        return extras.containsKey(AccountManager.KEY_ACCOUNT_NAME) &&
                extras.containsKey(AccountManager.KEY_ACCOUNT_TYPE);
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
        if (!isNewAccount()) {
            emailInput.setText(account.name);
            emailInput.setEnabled(false);
            passwordInput.requestFocus();
        }
        passwordInput = (EditText) findViewById(R.id.password_input);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.cta_view_switcher);
        //noinspection ConstantConditions
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountManager accountManager = AccountManager.get(getApplicationContext());
                if (isNewAccount()) {
                    addNewAccount(accountManager);
                } else {
                    confirmCredentials(accountManager);
                }
            }
        });
    }

    private void addNewAccount(AccountManager accountManager) {
        String accountType = getString(R.string.account_type);
        Bundle accountOptions = new Bundle();
        accountOptions.putString(Authenticator.ADD_ACCOUNT_USER_NAME, emailInput.getText().toString());
        accountOptions.putString(Authenticator.ADD_ACCOUNT_USER_PASSWORD, passwordInput.getText().toString());

        viewSwitcher.showNext();
        accountManager.addAccount(
                accountType,
                null,
                null,
                accountOptions,
                null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        viewSwitcher.showPrevious();

                        try {
                            final Bundle result = future.getResult();
                            if (result.containsKey(AccountManager.KEY_ACCOUNT_NAME)) {
                                setAccountAuthenticatorResult(result);
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Snackbar.make(findViewById(android.R.id.content),
                                        result.getString(AccountManager.KEY_ERROR_MESSAGE),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        }
                    }
                },
                null
        );
    }

    private void confirmCredentials(AccountManager accountManager) {
        Bundle options = new Bundle();
        options.putString(Authenticator.UPDATE_CREDENTIALS_USER_PASSWORD, passwordInput.getText().toString());

        viewSwitcher.showNext();
        accountManager.updateCredentials(
                account,
                getString(R.string.authtoken_type),
                options,
                null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        viewSwitcher.showPrevious();

                        try {
                            final Bundle result = future.getResult();
                            if (result.containsKey(AccountManager.KEY_ACCOUNT_NAME)) {
                                setAccountAuthenticatorResult(result);
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Snackbar.make(findViewById(android.R.id.content),
                                        result.getString(AccountManager.KEY_ERROR_MESSAGE),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        }
                    }
                },
                null
        );
    }

    public boolean isNewAccount() {
        return account == null;
    }
}