package com.sktlab.android.base.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

public class PermissionChecker {
    public static final String[] PERMISSION_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static final String[] PERMISSION_CAMERA = {
            Manifest.permission.CAMERA
    };

    public static final String[] PERMISSION_AUDIO = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    public static final String[] PERMISSION_CAMERA_AUDIO = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    public static boolean checkStoragePermission(Context context) {
        return checkPermission(context, PERMISSION_STORAGE);
    }

    public static boolean checkCameraPermission(Context context) {
        return checkPermission(context, PERMISSION_CAMERA);
    }

    public static boolean checkAudioPermission(Context context) {
        return checkPermission(context, PERMISSION_AUDIO);
    }

    public static boolean checkCameraAudioPermission(Context context) {
        return checkPermission(context, PERMISSION_CAMERA_AUDIO);
    }

    private static boolean checkPermission(Context context, String[] permissions) {
        for (String permission : permissions)
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        return true;
    }
}
