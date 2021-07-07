package com.sktlab.android.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.sktlab.android.base.Callback;
import com.sktlab.android.base.result.ResultImp;
import com.sktlab.android.base.threadpool.Executors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AppUtil {
    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * [获取应用包名]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图标 bitmap
     *
     * @param context
     */
    public static synchronized Bitmap getAppIcon(Context context) {
        PackageManager packageManager;
        ApplicationInfo applicationInfo;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        return bd.getBitmap();
    }

    public static boolean isLocationOpen(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isLocationEnabled();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE) != Settings.Secure.LOCATION_MODE_OFF;
            } catch (Settings.SettingNotFoundException e) {
                return false;
            }
        } else {
            return !StringUtil.isEmpty(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED));
        }
    }

    public static void goLocationSetting(Activity activity, int requestCode) {
        Intent intent;
        try {
            intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        } catch (Exception e) {
            intent = new Intent(Settings.ACTION_SETTINGS);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void hasNewerVersionOnGoogle(WeakReference<Context> contextRef, WeakReference<Callback> callbackRef) {
        Executors.net().execute(() -> {
            if (contextRef.get() == null || callbackRef.get() == null) {
                return;
            }
            boolean result = false;
            String packageName = getPackageName(contextRef.get());
            String currentVersion = getVersionName(contextRef.get());
            String url = "https://play.google.com/store/apps/details?id=" + packageName;
            HttpUrl httpUrl = HttpUrl.parse(url);
            if (httpUrl == null){
                Executors.main().execute(() -> {
                    if (callbackRef.get() != null) {
                        callbackRef.get().onComplete(ResultImp.failed());
                    }
                });
                return;
            }
            Request.Builder builder = new Request.Builder();
            builder.get().url(httpUrl);
            OkHttpClient client = HttpClient.defaultClient();
            BufferedReader reader = null;
            try {
                Response response = client.newCall(builder.build())
                        .execute();
                ResponseBody body = response.body();
                if (body == null) {
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(body.byteStream()));
                String line;
                StringBuilder content = new StringBuilder();
                String thatVersion = null;
                Pattern p = Pattern.compile("\"softwareVersion|当前版本\"\\W*([\\d\\.]+)");
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = p.matcher(line);
                    if (matcher.find()) {
                        Log.i("VersionOnGoogle", "version=" + matcher.group(1));
                        thatVersion = matcher.group(1);
                    }
                    content.append(line);
                }
                if (thatVersion != null && thatVersion.compareTo(currentVersion) > 0)
                    result = true;
                Log.i("VersionOnGoogle", content.toString());
                final boolean flag = result;
                Executors.main().execute(() -> {
                    if (callbackRef.get() != null) {
                        callbackRef.get().onComplete(ResultImp.success(flag));
                    }
                });
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Executors.main().execute(() -> {
                if (callbackRef.get() != null) {
                    callbackRef.get().onComplete(ResultImp.failed());
                }
            });
        });
    }
}
