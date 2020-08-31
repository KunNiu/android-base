package com.sktlab.android.base.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json {
    private static Gson gson = buildGson();

    private static Gson buildGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .disableHtmlEscaping()
                .create();
    }

    public static Gson get(){
        return gson;
    }
}
