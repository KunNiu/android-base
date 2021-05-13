package com.sktlab.android.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.sktlab.android.base.BuildConfig;

/**
 * Created by Nick from SKTLab on 2017/3/23.
 **/

public class SpUtil {
    private SharedPreferences sp;

    private SpUtil(Context context) {
        this.sp = context.getSharedPreferences(BuildConfig.LIBRARY_PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    public static SpUtil context(Context context) {
        return new SpUtil(context);
    }

    public void save(@NonNull String key, @NonNull String s) {
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, s);
        e.apply();
    }

    public void save(@NonNull String key, boolean b) {
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(key, b);
        e.apply();
    }

    public void save(@NonNull String key, float f) {
        SharedPreferences.Editor e = sp.edit();
        e.putFloat(key, f);
        e.apply();
    }

    public void save(@NonNull String key, int i) {
        SharedPreferences.Editor e = sp.edit();
        e.putInt(key, i);
        e.apply();
    }

    public void save(@NonNull String key, long l) {
        SharedPreferences.Editor e = sp.edit();
        e.putLong(key, l);
        e.apply();
    }

    public void remove(@NonNull String key) {
        SharedPreferences.Editor e = sp.edit();
        e.remove(key);
        e.apply();
    }

    public String getString(@NonNull String key) {
        return getString(key, null);
    }

    public String getString(@NonNull String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public boolean getBoolean(@NonNull String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public float getFloat(@NonNull String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public int getInt(@NonNull String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public long getLong(@NonNull String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public boolean contains(String key){
        return sp.contains(key);
    }
}
