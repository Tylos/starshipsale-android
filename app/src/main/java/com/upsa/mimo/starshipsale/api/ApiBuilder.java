package com.upsa.mimo.starshipsale.api;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.upsa.mimo.starshipsale.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Factory to build the Resource that will access to the Cabify REST API.
 * It builds Retrofit 'Services' that define the functions used to obtain info from the server
 * It also gives the user the capability to add the required headers to the request
 */
public class ApiBuilder<T> {

    public static final String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    private final Account account;
    private final String authTokenType;
    private AccountManager accountManager;
    private Class<T> typeParameterClass;
    private String serverURL;

    public ApiBuilder(Context context, Class<T> typeParameterClass, String serverURL) {
        this.typeParameterClass = typeParameterClass;
        this.serverURL = serverURL;
        this.accountManager = AccountManager.get(context);
        this.account = extractAccount(context);
        this.authTokenType = context.getString(R.string.authtoken_type);
    }

    @NonNull
    private Account extractAccount(Context context) {
        final Account[] accountsByType = accountManager.getAccountsByType(context.getString(R.string.account_type));
        if (accountsByType.length > 0) {
            return accountsByType[0];
        } else {
            return null;
        }
    }

    public T buildApiResource() {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(serverURL)
                .client(buildOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create());

        return retrofitBuilder.build().create(typeParameterClass);
    }

    private OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(addTokenInterceptor())
                .addInterceptor(getUnauthorizedInterceptor())
                .build();
    }

    private Interceptor addTokenInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                final Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .method(original.method(), original.body());

                if (account!=null) {
                    final String authToken = accountManager.peekAuthToken(account, authTokenType);
                    if (authToken != null) {
                        requestBuilder.addHeader(AUTH_TOKEN_HEADER, authToken);
                    }
                }
                return chain.proceed(requestBuilder.build());
            }
        };
    }

    @NonNull
    private Interceptor getUnauthorizedInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request originalRequest = chain.request();
                final Response originalResponse = chain.proceed(originalRequest);


                final boolean shouldRetry = isResponseFailureWithUnauthorized(originalResponse);
                if (shouldRetry) {
                    final String invalidToken = extractRequestAuthToken(originalRequest);
                    accountManager.invalidateAuthToken(account.type, invalidToken);
                    final String newAuthToken = issueToken();
                    if (newAuthToken != null) {
                        final Request updatedRequest = replaceTokenHeader(originalRequest, newAuthToken);
                        return chain.proceed(updatedRequest);
                    }
                 }

                return originalResponse;
            }

            private Request replaceTokenHeader(Request originalRequest, String newAuthToken) {
                return originalRequest.newBuilder()
                        .removeHeader(AUTH_TOKEN_HEADER)
                        .addHeader(AUTH_TOKEN_HEADER, newAuthToken)
                        .build();
            }

            private String issueToken() {
                try {
                    return accountManager.blockingGetAuthToken(account, authTokenType, false);
                } catch (Exception e) {
                    // User interaction, errors wtf
                    return null;
                }
            }

            private String extractRequestAuthToken(Request request) {
                return request.header(AUTH_TOKEN_HEADER);
            }

            private boolean isResponseFailureWithUnauthorized(Response response) {
                return !response.isSuccessful() && response.code() == 401;
            }
        };
    }

}