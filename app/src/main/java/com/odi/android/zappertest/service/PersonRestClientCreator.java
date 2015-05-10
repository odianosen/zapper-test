package com.odi.android.zappertest.service;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Odi on 2015-05-08.
 */
public class PersonRestClientCreator {
    private static final String BASE_SERVICE_URL = "http://demo3124542.mockable.io/candidatetest";
    private static final long HTTP_TIMEOUT = 30;

    public static PersonRestClient createClient() {
        return PersonRestClientCreator.createAdapter().create(PersonRestClient.class);
    }

    private static RestAdapter createAdapter() {
        final RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder()
                .setEndpoint(BASE_SERVICE_URL)
                .setClient(new OkClient(getOkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.BASIC);

        return restAdapterBuilder.build();
    }

    private static OkHttpClient getOkHttpClient() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS);

        return okHttpClient;
    }
}
