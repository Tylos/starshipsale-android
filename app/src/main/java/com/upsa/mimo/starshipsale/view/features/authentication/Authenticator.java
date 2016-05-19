package com.upsa.mimo.starshipsale.view.features.authentication;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.upsa.mimo.starshipsale.BuildConfig;
import com.upsa.mimo.starshipsale.api.session.SessionRepository;
import com.upsa.mimo.starshipsale.api.session.UnauthorizedException;
import com.upsa.mimo.starshipsale.domain.entities.Session;
import com.upsa.mimo.starshipsale.view.features.login.LoginActivity;

import java.io.IOException;

public class Authenticator extends AbstractAccountAuthenticator {

    public static final String ADD_ACCOUNT_USER_NAME = "add_account.user_name";
    public static final String ADD_ACCOUNT_USER_PASSWORD = "add_account.user_password";
    public static final String UPDATE_CREDENTIALS_USER_PASSWORD = "update_credentials.user_password";
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
        Bundle result = new Bundle();
        AccountManager accountManager = AccountManager.get(context);
        final String storedPassword = accountManager.getPassword(account);

        try {
            final Session session = sessionRepository.login(account.name, storedPassword);
            accountManager.setAuthToken(account, authTokenType, session.getToken());

            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, session.getToken());
            return result;
        } catch (IOException e) {
            result.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_NETWORK_ERROR);
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "Something went wrong " + e.getMessage());
            return result;
        } catch (UnauthorizedException e) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        AccountManager accountManager = AccountManager.get(context);
        final String userPassword = options.getString(UPDATE_CREDENTIALS_USER_PASSWORD);

        if (TextUtils.isEmpty(userPassword)) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, account.type);

            Bundle bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        } else {
            Bundle result = new Bundle();
            try {
                final Session session = sessionRepository.login(account.name, userPassword);
                accountManager.setPassword(account, userPassword);
                accountManager.setAuthToken(account, authTokenType, session.getToken());

                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                return result;
            } catch (Exception e) {
                result.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_NETWORK_ERROR);
                result.putString(AccountManager.KEY_ERROR_MESSAGE, "Something went wrong " + e.getMessage());
                return result;
            }
        }
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}