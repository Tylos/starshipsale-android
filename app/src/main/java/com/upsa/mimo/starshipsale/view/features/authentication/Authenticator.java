package com.upsa.mimo.starshipsale.view.features.authentication;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.upsa.mimo.starshipsale.BuildConfig;
import com.upsa.mimo.starshipsale.api.session.SessionRepository;
import com.upsa.mimo.starshipsale.view.features.login.LoginActivity;

public class Authenticator extends AbstractAccountAuthenticator {

    public static final String ADD_ACCOUNT_USER_NAME = "add_account.user_name";
    public static final String ADD_ACCOUNT_USER_PASSWORD = "add_account.user_password";
    private Context context;
    private SessionRepository sessionRepository;

    public Authenticator(Context context) {
        super(context);
        this.context = context;
        this.sessionRepository = new SessionRepository(context, BuildConfig.SERVER_REST_URL);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        if (requiresUserInteraction(options)) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        } else {
            final Bundle bundle = new Bundle();
            try {
                final String userName = options.getString(ADD_ACCOUNT_USER_NAME);
                final String userPassword = options.getString(ADD_ACCOUNT_USER_PASSWORD);

                sessionRepository.register(userName, userPassword);

                Account account = new Account(userName, accountType);
                AccountManager accountManager = AccountManager.get(context);
                accountManager.addAccountExplicitly(account, userPassword, null);

                bundle.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                return bundle;
            } catch (Exception e) {
                bundle.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_NETWORK_ERROR);
                bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "Something went wrong " + e.getMessage());
                return bundle;
            }
        }
    }

    private boolean requiresUserInteraction(Bundle options) {
        return options == null || !(options.containsKey(ADD_ACCOUNT_USER_NAME) && options.containsKey(ADD_ACCOUNT_USER_PASSWORD));
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}