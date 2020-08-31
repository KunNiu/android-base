package com.sktlab.android.base;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

public class AppChecker implements Application.ActivityLifecycleCallbacks {
    private static final Object LOCK = new Object();
    private static AppChecker instance;
    private static String pkgName;
    private static Set<ComponentName> activities = new HashSet<>();

    private AppChecker() {
    }

    static void init(@NonNull Application app) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new AppChecker();
                    pkgName = app.getPackageName();
                    app.registerActivityLifecycleCallbacks(instance);
                }
            }
        }
    }

    static void destroy(@NonNull Application app) {
        if (instance != null) {
            synchronized (LOCK) {
                if (instance != null) {
                    app.unregisterActivityLifecycleCallbacks(instance);
                    instance = null;
                }
            }
        }
    }

    private static void check() {
        if (instance == null) {
            throw new RuntimeException("Base library not initialized");
        }
    }

    public static boolean isActivityInStack(@NonNull Class<?> cls) {
        check();
        return activities.contains(ComponentName.createRelative(pkgName, cls.getName()));
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        activities.add(activity.getComponentName());
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        activities.remove(activity.getComponentName());
    }
}
