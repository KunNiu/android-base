package com.sktlab.android.base;

import android.app.Application;

public class Base {
    public static void init(Application app) {
        init(app, null);
    }

    public static void init(Application app, AppChecker.LifeMonitor lifeMonitor) {
        AppChecker.init(app, lifeMonitor);
    }

    public static void destroy(Application app) {
        AppChecker.destroy(app);
    }
}
