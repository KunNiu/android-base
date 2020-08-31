package com.sktlab.android.base.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;

public class Utils {
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public static boolean validatePassword(String password) {
        String regex = "^(?![0-9])(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    public static MediaType guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        fileName = fileName.replace("#", "");
        String contentType = fileNameMap.getContentTypeFor(fileName);
        if (contentType == null) {
            return MediaType.parse("application/octet-stream");
        }
        return MediaType.parse(contentType);
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static byte[] file2Bytes(File file) {
        RandomAccessFile rf = null;
        byte[] data = null;
        try {
            rf = new RandomAccessFile(file, "r");
            data = new byte[(int) rf.length()];
            rf.readFully(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rf != null) {
                    rf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
