package com.sktlab.android.base.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.sktlab.android.base.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpClient {
    private static final String TAG = HttpClient.class.getSimpleName();
    private static final int MAX_LOG_LENGTH = 1024;

    public static OkHttpClient.Builder newClient() {
        return new OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .addNetworkInterceptor(new HttpLoggingInterceptor(message -> {
                    if (message.length() > MAX_LOG_LENGTH) {
                        int chunkCount = message.length() / MAX_LOG_LENGTH;
                        for (int i = 0; i <= chunkCount; i++) {
                            int max = MAX_LOG_LENGTH * (i + 1);
                            if (max >= message.length()) {
                                Log.d(TAG, message.substring(MAX_LOG_LENGTH * i));
                            } else {
                                Log.d(TAG, message.substring(MAX_LOG_LENGTH * i, max));
                            }
                        }
                    } else {
                        Log.d(TAG, message);
                    }
                }).setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY: HttpLoggingInterceptor.Level.BASIC))
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(3, TimeUnit.MINUTES);
    }

    public static @NonNull
    OkHttpClient defaultClient() {
        return newClient().build();
    }
}
