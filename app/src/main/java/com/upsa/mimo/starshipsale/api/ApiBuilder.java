package com.upsa.mimo.starshipsale.api;

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

    private Class<T> mTypeParameterClass;

    private String mServerURL;

    public ApiBuilder(
            Class<T> typeParameterClass,
            String serverURL) {
        mTypeParameterClass = typeParameterClass;
        mServerURL = serverURL;
    }

    public T buildApiResource() {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(mServerURL)
                .client(buildOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create());

        return retrofitBuilder.build().create(mTypeParameterClass);

    }

    private OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        }).build();
    }

}