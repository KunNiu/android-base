package com.sktlab.android.base;

import android.app.Application;

public class Base {
    public static void init(Application app) {
        AppChecker.init(app);
    }

    public static void destroy(Application app) {
        AppChecker.destroy(app);
    }
}
